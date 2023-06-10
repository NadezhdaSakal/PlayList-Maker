package com.sakal.playlistmaker.player.domain.api

interface PlayerInteractor {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
}