package com.sakal.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.databinding.ItemSearchRecyclerBinding
import com.sakal.playlistmaker.search.domain.Track


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


