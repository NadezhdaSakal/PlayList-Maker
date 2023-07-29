package com.sakal.playlistmaker.search.domain.impl

import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.Resource
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.TracksRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class TracksInteractorImpl(private val repository: TracksRepo) : TracksInteractor {

    override fun searchTracks(query: String): Flow<Pair<ArrayList<Track>?, String?>> {
        return repository.searchTracks(query).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
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

    override fun getHistory(): ArrayList<Track> {
        return repository.getHistory()
    }
}