package com.sakal.playlistmaker.player.domain.model

sealed interface PlayerState {

    object Preparing : PlayerState

    object Stopped : PlayerState

    object Playing : PlayerState

    object Paused : PlayerState

    data class UpdatePlayingTime(
        val playingTime: String
    ) : PlayerState

}