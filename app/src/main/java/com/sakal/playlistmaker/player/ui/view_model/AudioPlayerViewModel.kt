package com.sakal.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.player.domain.TrackForPlayer
import com.sakal.playlistmaker.player.ui.PlayerScreenState
import com.sakal.playlistmaker.utils.formatTime
import java.util.*

class AudioPlayerViewModel(trackForPlayer: TrackForPlayer) : ViewModel() {

    private val _screenState = MutableLiveData<PlayerScreenState>()
    val screenState: LiveData<PlayerScreenState> = _screenState
    private val playerInteractor = Creator.providePlayerInteractor(trackForPlayer)
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT
    private val handler: Handler = Handler(Looper.getMainLooper())

            private val timerGo =
        object : Runnable {
            override fun run() {
                updateTimer(getCurrentPosition())
                handler.postDelayed(
                    this,
                    Constants.REFRESH_TIMER_DELAY,
                )
            }
        }
    init {
        _screenState.value = PlayerScreenState.BeginningState(trackForPlayer)
        preparePlayer()
        setOnCompletionListener()
    }
    private fun preparePlayer(){
        playerInteractor.preparePlayer {
            playerState = PlayerState.STATE_PREPARED
            _screenState.value = PlayerScreenState.Preparing()
        }
    }
    private fun setOnCompletionListener() {
        playerInteractor.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            handler.removeCallbacks(timerGo)
            _screenState.value = PlayerScreenState.PlayCompleting()
        }
    }
    private fun start() {
        playerInteractor.start()
        playerState = PlayerState.STATE_PLAYING
        handler.postDelayed(timerGo, Constants.REFRESH_TIMER_DELAY)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }
    fun pause() {
        playerInteractor.pause()
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacks(timerGo)
        _screenState.value = PlayerScreenState.PlayButtonHandling(playerState)
    }
    private fun updateTimer(time: String) {
        _screenState.postValue(PlayerScreenState.TimerUpdating(time))
    }

    private fun getCurrentPosition():String{
        return formatTime(playerInteractor.getCurrentTime())
    }

    fun onDestroy(){
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerInteractor.onDestroy()
    }

    fun playbackControl() {
        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                pause()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                start()
            }
            PlayerState.STATE_DEFAULT -> {

            }
        }
    }

    enum class PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

    companion object {
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(trackForPlayer: TrackForPlayer): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AudioPlayerViewModel(
                        trackForPlayer
                    ) as T
                }
            }
    }
}
