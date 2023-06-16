package com.sakal.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ActivityAudioplayerBinding
import com.sakal.playlistmaker.player.ui.PlayerScreenState
import com.sakal.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.sakal.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding

    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }

        initToolbar()

        @Suppress("DEPRECATION") val track = intent.getSerializableExtra(Constants.TRACK) as Track

        showTrack(track)

        binding.playTrack.isEnabled = false

        viewModel.preparePlayer(track.previewUrl)

        binding.playTrack.setOnClickListener {
            viewModel.playbackControl()
        }

    }

    private fun initToolbar() {
        binding.playerToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Preparing -> {
            }

            is PlayerScreenState.Stopped -> {
                binding.playTrack.isEnabled = true
                binding.playTrack.setImageResource(R.drawable.play_arrow)
                binding.progress.setText(R.string.playing_time)
            }

            is PlayerScreenState.Paused -> {
                binding.playTrack.setImageResource(R.drawable.play_arrow)
            }

            is PlayerScreenState.Playing -> {
                binding.playTrack.setImageResource(R.drawable.pause)
            }

            is PlayerScreenState.UpdatePlayingTime -> {
                binding.progress.text = state.playingTime
            }
        }
    }

    private fun showTrack(track: Track) {

        binding.apply {
            Glide
                .with(trackIcon)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_512)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.corner_radius_8
                        )
                    )
                )
                .into(trackIcon)

            trackName.text = track.trackName
            trackName.isSelected = true
            artistName.text = track.artistName
            trackName.isSelected = true
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country

            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                releaseDateData.text = formattedDatesString
            }

            if (track.collectionName.isNotEmpty()) {
                albumName.text = track.collectionName
            } else {
                albumName.visibility = View.GONE
                album.visibility = View.GONE

            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

}
