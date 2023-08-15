package com.sakal.playlistmaker.media_library.data.impl

import com.sakal.playlistmaker.media_library.data.db.DataBase
import com.sakal.playlistmaker.media_library.data.db.entity.RoomConverter
import com.sakal.playlistmaker.media_library.data.db.entity.TrackEntity
import com.sakal.playlistmaker.media_library.domain.FavoritesRepo
import com.sakal.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoritesRepoImpl(
    private val database: DataBase,
    private val converter: RoomConverter,
) : FavoritesRepo {
    override suspend fun saveTrack(track: Track) {
        database
            .tracksDao()
            .addTrack(converter.map(track))
    }

    override suspend fun deleteTrack(trackId: Int) {
        database
            .tracksDao()
            .deleteTrack(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = database
            .tracksDao().getFavoriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> = flow {
        val isInFavorite = database
            .tracksDao()
            .isFavorite(trackId)
        emit(isInFavorite)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }
}