package com.sakal.playlistmaker

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
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }

        findViewById<Button>(R.id.btn_search).setOnClickListener(buttonClickListener)

        findViewById<Button>(R.id.btn_media).setOnClickListener {
            startActivity(Intent(this@MainActivity, LibraryActivity::class.java))
        }

        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }

    }
}