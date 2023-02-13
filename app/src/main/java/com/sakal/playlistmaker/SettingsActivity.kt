package com.sakal.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)

        toolbar.setNavigationOnClickListener() {
            finish()
        }

        findViewById<Button>(R.id.button_sharing).setOnClickListener() {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
        }

        findViewById<Button>(R.id.button_support).setOnClickListener() {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
            startActivity(Intent.createChooser(intent, null))
        }

        findViewById<Button>(R.id.button_user_agreement).setOnClickListener() {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.support_user_agreement))
            startActivity(Intent.createChooser(intent, null))
        }

    }

}

