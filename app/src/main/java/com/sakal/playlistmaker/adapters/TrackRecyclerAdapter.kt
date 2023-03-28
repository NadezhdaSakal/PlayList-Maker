package com.sakal.playlistmaker.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.model.Track
import com.sakal.playlistmaker.viewHolders.TrackViewHolder

class TrackRecyclerAdapter ( private val tracks: List<Track>
) : RecyclerView.Adapter<TrackViewHolder> (){

    var itemClickListener: ((Int, Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener(){
            itemClickListener?.invoke(position, track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }


}

