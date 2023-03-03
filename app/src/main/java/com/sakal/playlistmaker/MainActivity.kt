package com.sakal.playlistmaker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                navigateTo(SearchActivity::class.java)
            }
        }

        findViewById<Button>(R.id.btn_search).setOnClickListener(buttonClickListener)

        findViewById<Button>(R.id.btn_media).setOnClickListener {
            navigateTo(LibraryActivity::class.java)
        }

        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

    }

    private fun navigateTo(clazz: Class<out Activity>) {
        startActivity(Intent(this@MainActivity, clazz))
    }
}