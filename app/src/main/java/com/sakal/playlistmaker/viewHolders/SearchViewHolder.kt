package com.sakal.playlistmaker.viewHolders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.model.Track
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import java.text.SimpleDateFormat
import java.util.*


class SearchViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemSearchRecyclerBinding.bind(itemView)

    fun bind(track: Track) = with(binding) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerCrop()
            .placeholder(R.drawable.track_icon)
            .transform(RoundedCorners(2))
            .into(trackIcon)

    }
}

