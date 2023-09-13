package com.sakal.playlistmaker.media_library.data.db.entity

import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track
import java.util.Calendar


class RoomConverter {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.artworkUrl60,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
        )
    }

    fun mapToEntity(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.artworkUrl60,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
            Calendar.getInstance().timeInMillis
        )
    }

    fun map(playlistWithCountTracks: PlaylistWithCountTracks): Playlist {
        playlistWithCountTracks.apply {
            return Playlist(
                playlistId!!,
                name,
                description,
                cover,
                tracksCount,
            )
        }
    }

    fun map(playListsTrackEntity: PlaylistsTrackEntity): Track {
        playListsTrackEntity.apply {
            return Track(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                artworkUrl60,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl
            )
        }
    }

    fun map(track: Track): PlaylistsTrackEntity {
        track.apply {
            return PlaylistsTrackEntity(
                trackId,
                trackName,
                artistName,
                trackTimeMillis,
                artworkUrl100,
                artworkUrl60,
                collectionName,
                releaseDate,
                primaryGenreName,
                country,
                previewUrl,
            )
        }
    }
}