package com.sakal.playlistmaker.player.domain.impl

import com.sakal.playlistmaker.player.domain.PlayerInteractor
import com.sakal.playlistmaker.player.domain.PlayerRepo


class PlayerInteractorImpl(private val playerRepository: PlayerRepo): PlayerInteractor {

    override fun start() {
        playerRepository.start()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun onDestroy() {
        playerRepository.onDestroy()
    }

    override fun setOnCompletionListener(onComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(onComplete)
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun preparePlayer(prepare: () -> Unit) {
        playerRepository.preparePlayer(prepare)
    }
}