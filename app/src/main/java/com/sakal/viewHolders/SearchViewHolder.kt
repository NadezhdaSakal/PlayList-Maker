package com.sakal.viewHolders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.data.Track

class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTrackTime)
    private val ava: ImageView = itemView.findViewById(R.id.trackAva)


    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = "${track.artistName} • ${track.trackTime}"

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_icon)
            .transform(RoundedCorners(2))
            .into(ava)

    }
}

