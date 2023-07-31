package com.sakal.playlistmaker.player.domain

interface PlayerInteractor {
    fun prepare(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun start()
    fun pause()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun releasePlayer()

}