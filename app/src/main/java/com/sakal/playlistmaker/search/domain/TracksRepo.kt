package com.sakal.playlistmaker.search.domain

import com.sakal.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepo {
        fun searchTracks(query: String): Flow<Resource<ArrayList<Track>>>
        fun addTrackToHistory(track: Track)
        fun clearHistory()
        fun getHistory(): ArrayList<Track>
}