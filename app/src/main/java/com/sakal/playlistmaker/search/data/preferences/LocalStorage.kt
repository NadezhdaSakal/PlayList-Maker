package com.sakal.playlistmaker.search.data.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.sakal.playlistmaker.search.domain.HistoryDataStore
import com.sakal.playlistmaker.search.domain.Track

class LocalStorage(private val sharedPreferences: SharedPreferences) :
    HistoryDataStore {
    private val TRACK_KEY = "Track_key"

    override fun clearHistory() {
        sharedPreferences.edit().remove(TRACK_KEY).apply()
    }
    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return emptyList()
        return Gson().fromJson(json, Array<Track>::class.java).toList()
    }
    override fun writeHistory(tracks: List<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit().putString(TRACK_KEY, json).apply()
    }
}



