package com.sakal.playlistmaker.player.data

import android.media.MediaPlayer

class Player(private val client: MediaPlayer) : PlayerClient {

    override fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {

        client.setDataSource(url)
        client.prepareAsync()
        client.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        client.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    override fun start() {
        client.start()
    }

    override fun pause() {
        client.pause()
    }

    override fun isPlaying(): Boolean {
        return client.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return client.currentPosition
    }

    override fun reset() {
        client.reset()
    }
}