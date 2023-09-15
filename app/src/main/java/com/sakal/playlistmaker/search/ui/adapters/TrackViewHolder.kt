package com.sakal.playlistmaker.search.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.millisConverter
import com.sakal.playlistmaker.utils.setImage

open class TrackViewHolder(private val binding: ItemSearchRecyclerBinding) :
    RecyclerView.ViewHolder(binding.root) {

    open fun bind(track: Track) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_2)
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTimeMillis.millisConverter()

        track.artworkUrl60?.let {
            binding.trackIcon.setImage(
                url = it,
                placeholder = R.drawable.placeholder_512,
                cornerRadius = cornerRadius,
            )
        }

    }
}
