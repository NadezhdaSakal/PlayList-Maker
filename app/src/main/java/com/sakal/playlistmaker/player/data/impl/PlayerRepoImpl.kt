package com.sakal.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.sakal.playlistmaker.player.domain.PlayerRepo


class PlayerRepoImpl: PlayerRepo {

    private var mediaPlayer = MediaPlayer()

    override fun prepare(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


}