package com.sakal.playlistmaker.search.data.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.data.ILocalStorage
import com.sakal.playlistmaker.search.domain.Track

class LocalStorage(private val preferences: SharedPreferences, private val gson: Gson) :
    ILocalStorage {

    override fun addToHistory(track: Track) {

        val searchedTracks = getHistory()
        searchedTracks.remove(track)
        searchedTracks.add(Constants.INDEX_FIRST, track)

        if (searchedTracks.size > Constants.HISTORY_LIST_SIZE)
            searchedTracks.removeLast()

        val json = gson.toJson(searchedTracks)
        preferences.edit { putString(Constants.HISTORY_TRACKS_KEY, json) }
    }

    override fun clearHistory() {
        preferences.edit { remove(Constants.HISTORY_TRACKS_KEY) }
    }

    override fun getHistory(): ArrayList<Track> {
        val json =
            preferences.getString(Constants.HISTORY_TRACKS_KEY, null) ?: return arrayListOf()
        return gson.fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
}





