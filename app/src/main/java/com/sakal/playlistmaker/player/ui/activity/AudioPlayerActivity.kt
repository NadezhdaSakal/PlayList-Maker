package com.sakal.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ActivityAudioplayerBinding
import com.sakal.playlistmaker.player.ui.PlayerScreenState
import com.sakal.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.sakal.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(Constants.TRACK) as Track

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeFavoriteState().observe(this) {
            render(it)
        }

        initToolbar()

        showTrack(track)

        viewModel.isFavorite(track.trackId)

        binding.buttonAddToFavorites.setOnClickListener {
            binding.buttonAddToFavorites.startAnimation(
                AnimationUtils.loadAnimation(
                    this@AudioPlayerActivity,
                    R.anim.scale
                )
            )
            viewModel.onFavoriteClicked(track)
        }

        binding.playTrack.isEnabled = false

        if (savedInstanceState == null) {
            viewModel.preparePlayer(track.previewUrl)
        }

        binding.playTrack.setOnClickListener {
            binding.playTrack.startAnimation(
                AnimationUtils.loadAnimation(
                    this@AudioPlayerActivity,
                    R.anim.scale
                )
            )
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
                binding.playTrack.isEnabled = true
                binding.playTrack.setImageResource(R.drawable.play_arrow)
            }

            is PlayerScreenState.Playing -> {
                binding.playTrack.isEnabled = true
                binding.playTrack.setImageResource(R.drawable.pause)
            }

            is PlayerScreenState.UpdatePlayingTime -> {
                binding.progress.text = state.playingTime
            }

            is PlayerScreenState.StateFavorite -> {
                if (state.isFavorite) {
                    binding.buttonAddToFavorites.setImageResource(R.drawable.like)
                } else {
                    binding.buttonAddToFavorites.setImageResource(R.drawable.unlike)
                }
            }

            is PlayerScreenState.Unplayable -> {
                binding.playTrack.isEnabled = false
                binding.playTrack.setImageResource(R.drawable.play_arrow)
                binding.progress.setText(R.string.playing_time)
            }
        }
    }

    private fun showTrack(track: Track) {
        binding.apply {
            Glide
                .with(trackIcon)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
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

            if (track.primaryGenreName != null) {
                primaryGenreName.text = track.primaryGenreName
            } else {
                genre.visibility = View.GONE
                primaryGenreName.visibility = View.GONE
            }

            if (track.country != null) {
                countryData.text = track.country
            } else {
                country.visibility = View.GONE
                countryData.visibility = View.GONE
            }

            if (track.trackTimeMillis != null) {
                trackTime.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            } else {
                trackTime.setText(R.string.playing_time)
            }

            if (track.releaseDate != null) {
                val parseDate =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
                releaseDateData.text =
                    parseDate?.let { date ->
                        SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                    }
            } else {
                year.visibility = View.GONE
                releaseDateData.visibility = View.GONE
            }

            if (track.collectionName != null) {
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