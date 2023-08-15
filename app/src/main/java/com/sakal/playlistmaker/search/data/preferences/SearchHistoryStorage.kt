package com.sakal.playlistmaker.search.data.preferences

import com.sakal.playlistmaker.search.domain.Track

interface SearchHistoryStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}