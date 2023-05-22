package com.sakal.playlistmaker.domain

import com.sakal.playlistmaker.data.MediaPlayerListener
import com.sakal.playlistmaker.data.PlayerRepo
import com.sakal.playlistmaker.domain.models.Track

class Interactor(private val playerStateListener: PlayerStateListener) : MediaPlayerListener {

    private val playerRepository = PlayerRepo(this)

    fun start() {
        playerRepository.start()
    }

    fun pause() {
        playerRepository.pause()
    }

    fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    fun onCompletionListener() {
        playerRepository.onCompletionListener()
    }

    fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun setStatePrepared() {
        playerStateListener.setStatePrepared()
    }

    override fun removeHandlersCallbacks() {
        playerStateListener.removeHandlersCallbacks()
    }

    override fun setImagePlay() {
        playerStateListener.setImagePlay()
    }

    override fun setCurrentTimeZero() {
        playerStateListener.setCurrentTimeZero()
    }

}