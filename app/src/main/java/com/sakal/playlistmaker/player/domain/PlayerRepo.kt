package com.sakal.playlistmaker.player.domain

interface PlayerRepo {
    fun prepare(url: String, onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun start()
    fun pause()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
    fun reset()

}