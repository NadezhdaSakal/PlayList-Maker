package com.sakal.playlistmaker.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.domain.Track
import com.sakal.playlistmaker.presentation.PlayerPresenter
import com.sakal.playlistmaker.presentation.PlayerView

class AudioPlayerActivity : AppCompatActivity(), PlayerView {

    private lateinit var toolbar: Toolbar
    private lateinit var trackName: TextView
    private lateinit var trackTime: TextView
    private lateinit var artistName: TextView
    private lateinit var albumIcon: ImageView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var buttonPlay: FloatingActionButton
    private lateinit var presenter: PlayerPresenter
    private lateinit var track: Track
    private lateinit var progress: TextView

    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        initToolbar()

        initTrackInfo()

        presenter.preparePlayer(track)

        presenter.onCompletionListener()

        buttonPlay.setOnClickListener {
            presenter.onClickPlayAndPause()
        }

    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.player_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initTrackInfo() {

        presenter = PlayerPresenter(this)

        track = gson.fromJson(
            intent.getStringExtra(Constants.TRACK),
            Track::class.java
        )

        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.trackTime)
        albumIcon = findViewById(R.id.track_icon)
        collectionName = findViewById(R.id.album_name)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name)
        country = findViewById(R.id.country_data)
        buttonPlay = findViewById(R.id.play_track)
        progress = findViewById(R.id.progress)

        Glide
            .with(albumIcon)
            .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder_512)
            .centerCrop()
            .transform(
                RoundedCorners(resources.getDimensionPixelSize(R.dimen.corner_radius_8))
            )
            .into(albumIcon)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        trackTime.text = presenter.millisFormat(track)
        releaseDate.text = presenter.fixReleaseDate(track.releaseDate)

        if (track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        } else {
            collectionName.visibility = View.GONE
            collectionNameTitle.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        presenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.clearPlayer()
    }

    override fun setImage(image: Int) {
        buttonPlay.setImageResource(image)
    }

    override fun setProgressTime(time: String) {
        progress.text = time
    }
}

