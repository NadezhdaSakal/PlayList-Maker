package com.sakal.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sakal.playlistmaker.model.Track


class SearchHistory(private val sharedPref: SharedPreferences) {


    fun add(track: Track) {
        val tracksHistory = get()
        tracksHistory.remove(track)
        tracksHistory.add(0, track)
        if (tracksHistory.size > 10) tracksHistory.removeLast()
        save(tracksHistory)
    }

    fun get(): ArrayList<Track> {
        val json = sharedPref.getString(Constants.HISTORY_TRACKS, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun clear() {
        val tracksHistory = ArrayList<Track>()
        save(tracksHistory)
    }

    private fun save(tracksHistory: MutableList<Track>) {
        val json = Gson().toJson(tracksHistory)
        sharedPref.edit()
            .putString(Constants.HISTORY_TRACKS, json)
            .apply()
    }
}
