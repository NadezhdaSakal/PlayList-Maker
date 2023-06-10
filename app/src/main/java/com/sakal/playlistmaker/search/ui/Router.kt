package com.sakal.playlistmaker.search.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.sakal.playlistmaker.Constants.TRACK
import com.sakal.playlistmaker.player.ui.activity.AudioPlayerActivity

class Router(private val activity: AppCompatActivity) {

    fun openTrack(trackId: String) {
        val playerIntent = Intent(activity, AudioPlayerActivity::class.java)
        playerIntent.putExtra(TRACK, trackId)
        activity.startActivity(playerIntent)
    }

    fun goBack() {
        activity.finish()
    }
}