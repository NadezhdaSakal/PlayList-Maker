package com.sakal.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.data.ILocalStorage
import com.sakal.playlistmaker.search.data.model.mapToTrack
import com.sakal.playlistmaker.search.data.model.mapToTrackToStorage
import com.sakal.playlistmaker.search.domain.Track

class LocalStorage(private val sharedPreferences: SharedPreferences) :
    ILocalStorage {

    override fun addToHistory(track: Track) {
        val searchedTracks = getHistory().map { it.mapToTrackToStorage() } as MutableList
        if (searchedTracks.contains(track.mapToTrackToStorage())){
            searchedTracks.remove(track)
        }
        searchedTracks.add(Constants.INDEX_FIRST,track)
        if (searchedTracks.size> Constants.HISTORY_LIST_SIZE){
            searchedTracks.removeLast()
        }
        val json = Gson().toJson(searchedTracks)
        sharedPreferences
            .edit()
            .putString(Constants.HISTORY_TRACKS, json)
            .apply()
    }

    override fun clearHistory() {
        sharedPreferences
            .edit()
            .clear()
            .apply()
    }

    override fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(Constants.HISTORY_TRACKS, null) ?: return arrayListOf()
        val tracksToStorage = Gson().fromJson(json, Array<TrackToStorage>::class.java).toCollection(ArrayList())
        return tracksToStorage.map { it.mapToTrack() }
    }
}



