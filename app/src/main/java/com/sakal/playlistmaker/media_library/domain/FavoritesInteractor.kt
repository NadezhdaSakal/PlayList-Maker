package com.sakal.playlistmaker.media_library.domain

import com.sakal.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrack(track: Track)
    suspend fun deleteTrack(trackId: Int)
    fun getFavoritesTracks(): Flow<List<Track>>
    fun isFavorite(trackId: Int): Flow<Boolean>

}