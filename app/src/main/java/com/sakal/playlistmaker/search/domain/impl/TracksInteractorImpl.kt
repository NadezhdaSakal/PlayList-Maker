package com.sakal.playlistmaker.search.domain.impl

import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.Resource
import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.TracksRepo
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepo): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(query)){
                is Resource.Success ->{consumer.consume(resource.data, null, resource.code)}
                is Resource.Error -> {consumer.consume(null, resource.message, resource.code)}
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
