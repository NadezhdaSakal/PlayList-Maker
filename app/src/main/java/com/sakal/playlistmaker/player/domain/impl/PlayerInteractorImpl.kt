package com.sakal.playlistmaker.player.domain.impl

import com.sakal.playlistmaker.player.domain.api.PlayerInteractor
import com.sakal.playlistmaker.player.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        repository.preparePlayer(url, onPreparedListener, onCompletionListener)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }
}