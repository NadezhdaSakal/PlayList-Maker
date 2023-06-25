package com.sakal.playlistmaker.media_library.ui

import androidx.appcompat.app.AppCompatActivity

class MediaRouter(private val activity : AppCompatActivity) {

    fun goBack() {
        activity.finish()
    }
}