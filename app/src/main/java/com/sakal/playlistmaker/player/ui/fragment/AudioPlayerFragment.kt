package com.sakal.playlistmaker.player.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentAudioplayerBinding
import com.sakal.playlistmaker.media_library.ui.bottom_sheet.PlaylistsBottomSheet
import com.sakal.playlistmaker.player.ui.state.PlayerScreenState
import com.sakal.playlistmaker.player.ui.viewmodel.AudioPlayerViewModel
import com.sakal.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    private lateinit var binding: FragmentAudioplayerBinding
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private lateinit var track: Track

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            renderLikeButton(it)
        }

        initToolbar()

        showTrack(track)

        viewModel.isFavorite(track.trackId)

        binding.buttonAddToFavorites.setOnClickListener { button ->
            (button as? ImageView)?.let { startAnimation(it) }
            viewModel.onFavoriteButtonClick(track)
        }

        binding.playTrack.isEnabled = false

        if (savedInstanceState == null) {
            viewModel.preparePlayer(track.previewUrl)
        }

        binding.playTrack.setOnClickListener { button ->
            (button as? ImageView)?.let { startAnimation(it) }
            viewModel.playbackControl()
        }

        initAddToPlaylistButton()
    }

    @Suppress("DEPRECATION")
    override fun onAttach(context: Context) {
        super.onAttach(context)
        track = arguments?.getSerializable(Constants.TRACK) as Track
    }

    private fun renderLikeButton(isFavorite: Boolean) {
        val imageResource = if (isFavorite) R.drawable.like
        else R.drawable.unlike
        binding.buttonAddToFavorites.setImageResource(imageResource)
    }

    private fun startAnimation(button: ImageView) {
        button.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.scale
            )
        )
    }

    private fun initToolbar() {
        binding.playerToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
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

            is PlayerScreenState.Unplayable -> {
                binding.playTrack.isEnabled = false
                binding.playTrack.setImageResource(R.drawable.play_arrow)
                binding.progress.setText(R.string.playing_time)
            }
        }
    }

    private fun initAddToPlaylistButton() {
        binding.buttonAddToPlaylist.setOnClickListener { button ->
            (button as? ImageView)?.let { startAnimation(it) }
            PlaylistsBottomSheet.newInstance(track).show(childFragmentManager, PlaylistsBottomSheet.TAG)

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