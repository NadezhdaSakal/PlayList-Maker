package com.sakal.playlistmaker.player.data

interface PlayerClient {
    fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    )

    fun start()

    fun pause()

    fun isPlaying(): Boolean

    fun getCurrentPosition(): Int
}