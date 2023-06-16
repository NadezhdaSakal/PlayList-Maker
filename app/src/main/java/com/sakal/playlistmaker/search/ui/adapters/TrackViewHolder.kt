package com.sakal.playlistmaker.search.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import com.sakal.playlistmaker.search.domain.Track
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(private val binding: ItemSearchRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) = with (binding) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        Glide
            .with(itemView)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.track_icon)
            .transform(
                CenterCrop(), RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(
                    R.dimen.corner_radius_2))
            )
            .into(trackIcon)

    }
}
