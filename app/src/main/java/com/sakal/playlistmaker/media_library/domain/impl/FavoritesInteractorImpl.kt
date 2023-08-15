package com.sakal.playlistmaker.media_library.domain.impl

import com.sakal.playlistmaker.media_library.domain.FavoritesInteractor
import com.sakal.playlistmaker.media_library.domain.FavoritesRepo
import com.sakal.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(
    private val repository: FavoritesRepo,
) : FavoritesInteractor {

    override suspend fun addTrack(track: Track) {
        repository.saveTrack(track)
    }

    override suspend fun deleteTrack(trackId: Int) {
        repository.deleteTrack(trackId)
    }

    override fun getFavoritesTracks(): Flow<List<Track>> {
        return repository.getFavoritesTracks()
    }

    override fun isFavorite(trackId: Int): Flow<Boolean> {
        return repository.isFavorite(trackId)
    }
}
