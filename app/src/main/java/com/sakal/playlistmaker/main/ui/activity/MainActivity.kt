package com.sakal.playlistmaker.main.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.media_library.ui.activity.LibraryActivity
import com.sakal.playlistmaker.search.ui.activity.SearchActivity
import com.sakal.playlistmaker.settings.ui.activity.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        findViewById<Button>(R.id.btn_media).setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }
        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}