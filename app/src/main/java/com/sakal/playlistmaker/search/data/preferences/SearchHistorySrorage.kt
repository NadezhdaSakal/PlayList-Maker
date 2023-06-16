package com.sakal.playlistmaker.search.data.preferences

import com.sakal.playlistmaker.search.domain.Track

interface SearchHistorySrorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}