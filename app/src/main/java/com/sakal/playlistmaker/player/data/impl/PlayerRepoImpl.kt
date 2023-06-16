package com.sakal.playlistmaker.player.data.impl

import com.sakal.playlistmaker.player.data.PlayerClient
import com.sakal.playlistmaker.player.domain.PlayerRepo


class PlayerRepoImpl(private val client: PlayerClient): PlayerRepo {

    override fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        client.prepare(
            url = url,
            onPreparedListener = onPreparedListener,
            onCompletionListener = onCompletionListener
        )
    }

    override fun start() {
        client.start()
    }

    override fun pause() {
        client.pause()
    }

    override fun isPlaying(): Boolean = client.isPlaying()

    override fun getCurrentPosition(): Int = client.getCurrentPosition()

}