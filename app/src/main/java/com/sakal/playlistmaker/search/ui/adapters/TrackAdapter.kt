package com.sakal.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.utils.formatTime


class TrackAdapter(private val onClickListener: TrackClickListener): RecyclerView.Adapter<TrackViewHolder> () {

    private val tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = ItemSearchRecyclerBinding.inflate(layoutInspector, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { onClickListener.onTrackClick(tracks[position]) }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(tracks: Track)
    }

    fun setTracks(newTrackList: List<Track>?) {
        if (tracks.isNotEmpty()) {
            tracks.clear()
        }
        newTrackList?.let {
            tracks.addAll(it)
        }
        notifyDataSetChanged()
    }
}

class TrackViewHolder(private val binding: ItemSearchRecyclerBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTime?.let { formatTime(it.toInt()) }

        Glide
            .with(itemView)
            .load(track.image)
            .placeholder(R.drawable.track_icon)
            .transform(CenterCrop(), RoundedCorners(binding.trackIcon.resources.getDimensionPixelSize(R.dimen.corner_radius_2)))
            .into(binding.trackIcon)

    }
}

