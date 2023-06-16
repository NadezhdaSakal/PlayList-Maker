package com.sakal.playlistmaker.search.ui

import android.app.Activity
import android.content.Intent
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.sakal.playlistmaker.search.domain.Track

class Router(private val activity: Activity) {

    fun goBack() {
        activity.finish()
    }

    fun openAudioPlayer(track: Track) {
        val intent = Intent(activity, AudioPlayerActivity::class.java).apply {
            putExtra(Constants.TRACK, track)
        }
        activity.startActivity(intent)
    }
}


