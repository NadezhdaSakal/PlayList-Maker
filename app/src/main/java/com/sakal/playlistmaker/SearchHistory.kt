package com.sakal.playlistmaker

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sakal.playlistmaker.model.Track


class SearchHistory(private val preferences: SharedPreferences) {

    fun add(track: Track) {
        val history = get()
        history.remove(track)
        history.add(0, track)
        if (history.size > Constants.HISTORY_LIST_SIZE) history.removeLast()
        save(history)
    }

    fun get(): ArrayList<Track> {
        val json = preferences.getString(Constants.HISTORY_TRACKS, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track>>() {}.type)
    }

    fun clear() {
        preferences.edit { remove(Constants.HISTORY_TRACKS) }
    }

    private fun save(history: MutableList<Track>) {
        val json = Gson().toJson(history)
        preferences.edit { putString(Constants.HISTORY_TRACKS, json) }
    }
}
