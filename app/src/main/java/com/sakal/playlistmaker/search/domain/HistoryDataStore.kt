package com.sakal.playlistmaker.search.domain

interface HistoryDataStore {
    fun clearHistory()
    fun getHistory(): List<Track>
    fun writeHistory(tracks: List<Track>)
}