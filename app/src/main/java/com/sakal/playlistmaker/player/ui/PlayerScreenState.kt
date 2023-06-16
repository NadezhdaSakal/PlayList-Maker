package com.sakal.playlistmaker.player.ui

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ActivityAudioplayerBinding
import com.sakal.playlistmaker.player.domain.TrackForPlayer
import com.sakal.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.sakal.playlistmaker.utils.unparseDateToYear
import java.text.SimpleDateFormat
import java.util.*

sealed class PlayerScreenState {


    class BeginningState(val track: TrackForPlayer): PlayerScreenState() {
        override fun render(binding: ActivityAudioplayerBinding) {
            binding.trackName.text = track.trackName
            binding.artistName.text = track.artistName
            binding.trackTime.text = SimpleDateFormat(
                binding.trackTime.resources.getString(R.string.track_duration_time_format),
                Locale.getDefault()
            ).format(track.trackTime?.toLong() ?: 0L)
            binding.albumName.text = track.album
            binding.releaseDateData.text = unparseDateToYear(track.year!!)
            binding.primaryGenreName.text = track.genre
            binding.countryData.text = track.country

            Glide
                .with(binding.trackIcon)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.placeholder_512)
                .centerCrop()
                .transform(
                    RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(R.dimen.corner_radius_8))
                )
                .into(binding.trackIcon)
        }
    }

    class PlayButtonHandling(private val playerState: AudioPlayerViewModel.PlayerState) : PlayerScreenState() {
        override fun render(binding: ActivityAudioplayerBinding) {
            when (playerState) {
                AudioPlayerViewModel.PlayerState.STATE_PLAYING -> {
                    binding.playTrack.setImageResource(R.drawable.pause)
                }
                else -> {
                    binding.playTrack.setImageResource(R.drawable.play_arrow)
                }
            }
        }
    }

    class Preparing: PlayerScreenState(){
        override fun render(binding: ActivityAudioplayerBinding) {
            binding.playTrack.isEnabled = true
        }
    }
    class TimerUpdating(private val time: String): PlayerScreenState(){
        override fun render(binding: ActivityAudioplayerBinding) {
            binding.progress.text = time
        }
    }
    class PlayCompleting: PlayerScreenState(){
        override fun render(binding: ActivityAudioplayerBinding) {
            binding.progress.text = binding.progress.resources.getText(R.string.playing_time)
            binding.playTrack.setImageResource(R.drawable.play_arrow)
        }
    }

    abstract fun render(binding: ActivityAudioplayerBinding)
}