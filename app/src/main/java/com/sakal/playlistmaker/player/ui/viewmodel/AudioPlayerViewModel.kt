package com.sakal.playlistmaker.player.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.domain.PlayerInteractor
import com.sakal.playlistmaker.player.ui.PlayerScreenState
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackInteractor: TracksInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerScreenState>()

    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private var progressTimer: Job? = null

    fun preparePlayer(url: String?) {
        renderState(PlayerScreenState.Preparing)
        if (url != null) {
            playerInteractor.prepare(
                url = url,
                onPreparedListener = {
                    renderState(PlayerScreenState.Stopped)
                },
                onCompletionListener = {
                    progressTimer?.cancel()
                    renderState(PlayerScreenState.Stopped)
                }
            )
        } else {
            renderState(PlayerScreenState.Unplayable)
        }
    }

    private fun startPlayer() {
        playerInteractor.start()
        renderState(PlayerScreenState.Playing)
        updatePlayingTime()
    }

    fun pausePlayer() {
        playerInteractor.pause()
        renderState(PlayerScreenState.Paused)
        progressTimer?.cancel()    }

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
        progressTimer = viewModelScope.launch {
            while (isPlaying()) {
                delay(Constants.REFRESH_TIMER_DELAY)
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
            }
        }
    }

    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }

    fun getTrack(): Track {
        return trackInteractor
            .getHistory()
            .first()
    }
}