package com.sakal.playlistmaker.search.ui

import android.content.Intent
import com.google.gson.Gson
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.activity.SearchActivity

class Router(private val activity: SearchActivity) {

    fun sendToMedia(track: Track) {
        val searchIntent = Intent(activity, AudioPlayerActivity::class.java).apply {
            putExtra(Constants.TRACK, Gson().toJson(track))
        }
        activity.startActivity(searchIntent)
    }

    fun goBack() {
        activity.finish()
    }
}


