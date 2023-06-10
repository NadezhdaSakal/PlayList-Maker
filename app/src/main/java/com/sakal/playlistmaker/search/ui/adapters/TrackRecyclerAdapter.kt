package com.sakal.playlistmaker.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.viewHolders.TrackViewHolder


class TrackRecyclerAdapter(private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    fun updateTracks(newTracks: List<Track> = listOf()) {

        val oldTracks = this.tracks
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = oldTracks.size
            override fun getNewListSize(): Int = newTracks.size
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTracks[oldItemPosition].trackId == newTracks[newItemPosition].trackId

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                oldTracks[oldItemPosition].trackName == newTracks[newItemPosition].trackName
        })
        this.tracks.clear()
        this.tracks.addAll(newTracks)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_recycler, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }
}







