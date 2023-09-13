package com.sakal.playlistmaker.media_library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "track_playlists")
data class PlaylistsTrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)