package com.sakal.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.sakal.playlistmaker.player.domain.api.PlayerRepository


class PlayerRepositoryImpl : PlayerRepository {

    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(
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

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}