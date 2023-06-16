package com.sakal.playlistmaker.search.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.formatTime

class TrackViewHolder(private val binding: ItemSearchRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTime?.let { formatTime(it.toInt()) }

        Glide
            .with(itemView)
            .load(track.image)
            .placeholder(R.drawable.track_icon)
            .transform(
                CenterCrop(), RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(
                    R.dimen.corner_radius_2))
            )
            .into(binding.trackIcon)

    }
}
