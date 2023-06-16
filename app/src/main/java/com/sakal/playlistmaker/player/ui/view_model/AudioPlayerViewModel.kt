package com.sakal.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.player.ui.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerViewModel : ViewModel() {

    private val playerInteractor = Creator.providePlayerInteractor()

    private val stateLiveData = MutableLiveData<PlayerScreenState>()

    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val updatePlayingTimeRunnable = Runnable { updatePlayingTime() }

    fun preparePlayer(url: String) {
        renderState(PlayerScreenState.Preparing)
        playerInteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                renderState(PlayerScreenState.Stopped)
            },
            onCompletionListener = {
                handler.removeCallbacks(updatePlayingTimeRunnable)
                renderState(PlayerScreenState.Stopped)
            }
        )
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.Playing)
        handler.postDelayed(updatePlayingTimeRunnable, Constants.REFRESH_TIMER_DELAY)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.Paused)
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
            PlayerScreenState.UpdatePlayingTime(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(
                    getCurrentPosition()
                )
            )
        )
        handler.postDelayed(updatePlayingTimeRunnable, Constants.REFRESH_TIMER_DELAY)
    }

    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
    }


}