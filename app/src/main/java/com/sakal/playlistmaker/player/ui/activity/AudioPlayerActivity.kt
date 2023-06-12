package com.sakal.playlistmaker.player.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.databinding.ActivityAudioplayerBinding
import com.sakal.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.sakal.playlistmaker.search.data.model.mapTrackToTrackForPlayer
import com.sakal.playlistmaker.search.domain.Track
import java.util.*

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION") val track = intent.getSerializableExtra(Constants.TRACK) as Track

        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory(trackForPlayer = track.mapTrackToTrackForPlayer())
        )[AudioPlayerViewModel::class.java]


        binding.playerToolbar.setNavigationOnClickListener {
            finish()
            }


        binding.playTrack.apply {
            setOnClickListener {
                viewModel.playbackControl()
            }
        }

        viewModel.screenState.observe(this) {
            it.render(binding)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }
}
