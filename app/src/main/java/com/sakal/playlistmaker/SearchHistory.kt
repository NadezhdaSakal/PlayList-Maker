package com.sakal.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sakal.playlistmaker.model.Track


class SearchHistory (private val sharedPref: SharedPreferences){

    private val typeTokenArrayList = object : TypeToken<ArrayList<Track>>() {}.type

    fun addTrack(track: Track, position: Int){
        val jsonHistoryTracks = sharedPref.getString(Constants.HISTORY_TRACKS_KEY, null)
        if (jsonHistoryTracks == null){
            sharedPref.edit().putString(Constants.HISTORY_TRACKS_KEY, Gson().toJson(listOf(track))).apply()
            return
        }

        val historyTracks = Gson().fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)

        if (historyTracks.find { it.trackId == track.trackId } != null){
            historyTracks.remove(track)
            historyTracks.add(0, track)
            saveTrackForHistory(historyTracks)
            return
        }

        if (historyTracks.size == 10){
            historyTracks.remove(historyTracks[9])
        }

        historyTracks.add(0, track)
        saveTrackForHistory(historyTracks)
    }

    fun tracksHistoryFromJson(): List<Track> {
        val jsonHistoryTracks = sharedPref.getString(Constants.HISTORY_TRACKS_KEY, null) ?: return ArrayList<Track>()
        return Gson().fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)
    }

    fun clearHistory(){
        sharedPref.edit().remove(Constants.HISTORY_TRACKS_KEY).apply()
    }

    fun saveTrackForHistory(historyTracks : ArrayList<Track>){
        sharedPref.edit().putString(Constants.HISTORY_TRACKS_KEY, Gson().toJson(historyTracks)).apply()
    }
}
