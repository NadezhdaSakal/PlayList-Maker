package com.sakal.playlistmaker.media_library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "favorites_tracks")
data class TrackEntity(
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long?,
    val artworkUrl100: String?,
    val collectionName: String?,
    val country: String?,
    val primaryGenreName: String?,
    val releaseDate: String?,
    val previewUrl: String?,
    val saveDate: Date,
)