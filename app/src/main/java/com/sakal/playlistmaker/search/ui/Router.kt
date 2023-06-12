package com.sakal.playlistmaker.search.ui

import com.sakal.playlistmaker.search.ui.activity.SearchActivity

class Router(private val activity: SearchActivity) {

    fun goBack() {
        activity.finish()
    }
}


