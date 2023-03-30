package com.sakal.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var themeSwitcher: Switch
    private lateinit var buttonSharing: Button
    private lateinit var buttonSupport: Button
    private lateinit var buttonUserAgreement: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        initToolbar()

        initSwitch()

        initButtonSharing()

        initButtonSupport()

        initButtonUserAgreement()

    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.settings_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initSwitch() {
        themeSwitcher = findViewById(R.id.themeSwitcher)

        themeSwitcher.isChecked = getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, MODE_PRIVATE)
            .getBoolean(Constants.DARK_THEME_KEY, false)

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            (applicationContext as App).saveTheme(checked)
        }
    }

    private fun initButtonSharing() {
        buttonSharing = findViewById(R.id.button_sharing)
        buttonSharing.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
                type = "text/plain"
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initButtonSupport() {
        buttonSupport = findViewById(R.id.button_support)
        buttonSupport.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_address)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_message))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

    private fun initButtonUserAgreement() {
        buttonUserAgreement = findViewById(R.id.button_user_agreement)
        buttonUserAgreement.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.support_user_agreement))
                startActivity(Intent.createChooser(this, null))
            }
        }
    }

}

