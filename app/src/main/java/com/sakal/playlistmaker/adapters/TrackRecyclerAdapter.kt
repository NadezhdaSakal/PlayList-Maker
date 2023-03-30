package com.sakal.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.TracksDiffCallback
import com.sakal.playlistmaker.model.Track
import com.sakal.playlistmaker.viewHolders.TrackViewHolder

class TrackRecyclerAdapter(private val clickListener: TrackClickListener) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
        set(newTrackList) {
            val diffResult = DiffUtil.calculateDiff(
                TracksDiffCallback(field, newTrackList)
            )
            field = newTrackList
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_recycler, parent, false)
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





