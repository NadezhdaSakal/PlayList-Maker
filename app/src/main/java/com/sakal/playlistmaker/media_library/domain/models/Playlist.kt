package com.sakal.playlistmaker.media_library.domain.models

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    val name: String,
    val description: String,
    val cover: String?,
    var tracksCount: Int
) : Serializable
 {

    override fun equals(other: Any?): Boolean {
        return if (other !is Playlist) {
            false
        } else {
            other.playlistId == playlistId
        }
    }

    override fun hashCode(): Int {
        return playlistId
    }
}





