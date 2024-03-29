package com.sakal.playlistmaker.search.domain

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: Long,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
) : Serializable
{

    override fun equals(other: Any?): Boolean {
        return if (other !is Track) {
            false
        } else {
            other.trackId == trackId
        }
    }

    override fun hashCode(): Int {
        return trackId
    }
}