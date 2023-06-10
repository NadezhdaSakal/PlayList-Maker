package com.sakal.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel(application: Application) : AndroidViewModel(application) {
    private val playerInteractor = Creator.providePlayerInteractor()

    private val stateLiveData = MutableLiveData<PlayerState>()

    fun observeState(): LiveData<PlayerState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val updatePlayingTimeRunnable = Runnable { updatePlayingTime() }

    fun preparePlayer(url: String) {
        renderState(PlayerState.Preparing)
        playerInteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                renderState(PlayerState.Stopped)
            },
            onCompletionListener = {
                handler.removeCallbacks(updatePlayingTimeRunnable)
                renderState(PlayerState.Stopped)
            }
        )
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerState.Playing)
        handler.postDelayed(updatePlayingTimeRunnable, UPDATE_PLAYING_TIME_DELAY)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerState.Paused)
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    private fun isPlaying(): Boolean {
        return playerInteractor.isPlaying()
    }

    private fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun playbackControl() {
        if (isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun updatePlayingTime() {
        renderState(
            PlayerState.UpdatePlayingTime(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(
                    getCurrentPosition()
                )
            )
        )
        handler.postDelayed(updatePlayingTimeRunnable, UPDATE_PLAYING_TIME_DELAY)
    }

    private fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val UPDATE_PLAYING_TIME_DELAY = 300L
    }
}

