package com.sakal.playlistmaker.player.domain

interface PlayerRepo {
    fun preparePlayer(prepare: () -> Unit)

    fun setOnCompletionListener(onComplete: () -> Unit)

    fun start()

    fun pause()

    fun onDestroy()

    fun getCurrentTime(): Int
}