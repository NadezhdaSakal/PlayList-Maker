package com.sakal.playlistmaker.presentation

import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.domain.Interactor
import com.sakal.playlistmaker.domain.PlayerStateListener
import android.os.Handler
import android.os.Looper
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.domain.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerPresenter(private val view: PlayerView) : PlayerStateListener {

    private val play = R.drawable.play_arrow
    private val pause = R.drawable.pause
    private val interactor = Interactor(this)

    val mainHandler: Handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT

    private val runThread = object : Runnable {
        override fun run() {
            val currentTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(interactor.getCurrentTime())
            view.setProgressTime(currentTime)

            mainHandler.postDelayed(
                this,
                Constants.RELOAD_PROGRESS
            )
        }
    }

    fun onClickPlayAndPause() {
        progressControl()
        progressTimeControl()
    }

    fun preparePlayer(track: Track) {
        interactor.preparePlayer(track)
    }

    private fun progressControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        interactor.start()
        view.setImage(pause)
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        interactor.pause()
        view.setImage(play)
        playerState = STATE_PAUSED
    }

    fun onCompletionListener() {
        interactor.onCompletionListener()
    }

    private fun progressTimeControl() {
        when (playerState) {
            STATE_PLAYING -> {
                mainHandler.postDelayed(
                    runThread,
                    Constants.RELOAD_PROGRESS
                )
            }
            STATE_PAUSED -> {
                mainHandler.removeCallbacks(runThread)
            }
        }
    }

    fun clearPlayer() {
        interactor.releasePlayer()
        mainHandler.removeCallbacks(runThread)
    }

    fun fixReleaseDate(string: String): String = string.removeRange(4 until string.length)

    fun millisFormat(track: Track): String =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())

    override fun setStatePrepared() {
        view.setImage(play)
        playerState = STATE_PREPARED
    }

    override fun removeHandlersCallbacks() {
        mainHandler.removeCallbacks(runThread)
    }

    override fun setImagePlay() {
        view.setImage(play)
    }

    override fun setCurrentTimeZero() {
        view.setProgressTime(Constants.CURRENT_TIME_ZERO)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}