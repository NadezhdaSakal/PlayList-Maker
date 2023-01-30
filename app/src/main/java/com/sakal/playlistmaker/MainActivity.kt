package com.sakal.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Поиск", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btn_search).setOnClickListener(buttonClickListener)

        findViewById<Button>(R.id.btn_media).setOnClickListener {
            Toast.makeText(this@MainActivity, "Медиатека", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_settings).setOnClickListener {
            Toast.makeText(this@MainActivity, "Настройки", Toast.LENGTH_SHORT).show()
        }

    }
}