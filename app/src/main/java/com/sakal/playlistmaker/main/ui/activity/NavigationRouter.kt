package com.sakal.playlistmaker.main.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.sakal.playlistmaker.media_library.ui.activity.LibraryActivity
import com.sakal.playlistmaker.search.ui.activity.SearchActivity
import com.sakal.playlistmaker.settings.ui.activity.SettingsActivity

class NavigationRouter(private val activity: AppCompatActivity) {

    fun toSearch() {
        val serachIntent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(serachIntent)
    }

    fun toMediaLibrary() {
        val mediaLibraryIntent = Intent(activity, LibraryActivity::class.java)
        activity.startActivity(mediaLibraryIntent)
    }

    fun toSettings() {
        val settingsIntent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(settingsIntent)
    }
}