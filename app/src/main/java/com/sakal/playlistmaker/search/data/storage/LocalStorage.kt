package com.sakal.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.data.ILocalStorage
import com.sakal.playlistmaker.search.data.model.mapToTrackToStorage
import com.sakal.playlistmaker.search.domain.Track

class LocalStorage(private val preferences: SharedPreferences) :
    ILocalStorage {

    override fun addToHistory(track: Track) {

        val searchedTracks = getHistory().map { it.mapToTrackToStorage() } as MutableList
        if (searchedTracks.contains(track.mapToTrackToStorage())) {
            searchedTracks.remove(track)
        }
        searchedTracks.add(Constants.INDEX_FIRST, track)
        if (searchedTracks.size > Constants.HISTORY_LIST_SIZE) {
            searchedTracks.removeLast()
        }

        val json = Gson().toJson(searchedTracks)
        preferences
            .edit()
            .putString(Constants.HISTORY_TRACKS_KEY, json)
            .apply()
    }


    override fun clearHistory() {
        preferences
            .edit()
            .clear()
            .apply()
    }

    override fun getHistory(): ArrayList<Track> {
        val json =
            preferences.getString(Constants.HISTORY_TRACKS_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }
}





