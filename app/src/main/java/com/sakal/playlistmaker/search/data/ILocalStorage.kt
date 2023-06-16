package com.sakal.playlistmaker.search.data

import com.sakal.playlistmaker.search.domain.Track

interface ILocalStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): ArrayList<Track>
}