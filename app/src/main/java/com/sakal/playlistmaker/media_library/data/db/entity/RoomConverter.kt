package com.sakal.playlistmaker.media_library.data.db.entity

import com.sakal.playlistmaker.search.domain.Track
import java.util.Date

class RoomConverter {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
            saveDate = Date()
        )
    }
}
