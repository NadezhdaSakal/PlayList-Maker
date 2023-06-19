package com.sakal.playlistmaker.player.domain.impl

import com.sakal.playlistmaker.player.domain.PlayerInteractor
import com.sakal.playlistmaker.player.domain.PlayerRepo

class PlayerInteractorImpl(private val repository: PlayerRepo) : PlayerInteractor {

    override fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        repository.prepare(url, onPreparedListener, onCompletionListener)
    }

    override fun start() {
        repository.start()
    }

    override fun pause() {
        repository.pause()
    }

    override fun isPlaying(): Boolean {
        return repository.isPlaying()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }
}
