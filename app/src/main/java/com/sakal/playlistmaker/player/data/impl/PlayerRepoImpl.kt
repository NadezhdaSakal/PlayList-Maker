package com.sakal.playlistmaker.player.data.impl

import com.sakal.playlistmaker.player.data.PlayerClient
import com.sakal.playlistmaker.player.domain.PlayerRepo


class PlayerRepoImpl(private val playerClient: PlayerClient): PlayerRepo {
    override fun preparePlayer(prepare: () -> Unit) {
        playerClient.preparePlayer(prepare)
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerClient.setOnCompletionListener(onComplete)
    }

    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Int {
        return playerClient.getCurrentTime()
    }
}