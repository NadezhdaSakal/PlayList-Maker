package com.sakal.playlistmaker.search.domain.impl

import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.Resource
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.TracksRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TracksInteractorImpl(private val repository: TracksRepo) : TracksInteractor {

    override fun searchTracks(query: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.error)
                }
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }


    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return repository.getHistory()
    }
}