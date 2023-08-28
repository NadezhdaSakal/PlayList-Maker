package com.sakal.playlistmaker.media_library.data.db.entity

import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track
import kotlinx.serialization.json.Json
import java.util.Calendar
import kotlinx.serialization.encodeToString


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
            Calendar.getInstance().timeInMillis
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return with(playlist) {
            PlaylistEntity(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                imageUrl = coverImageUrl,
                trackList = Json.encodeToString(trackList),
                countTracks = tracksCount,
                Calendar.getInstance().timeInMillis,
            )
        }
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return with(playlist) {
            Playlist(
                id = id,
                playlistName = playlistName,
                playlistDescription = playlistDescription,
                coverImageUrl = imageUrl,
                trackList = Json.decodeFromString(trackList),
                tracksCount = countTracks,
            )
        }
    }
}

