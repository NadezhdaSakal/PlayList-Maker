package com.sakal.playlistmaker.media_library.ui.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.media_library.domain.models.Playlist


abstract class PlaylistsAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    var playlists = listOf<Playlist>()

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder


    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlistItem = playlists[holder.adapterPosition]
        holder.bind(playlistItem)
        holder.itemView.setOnClickListener { clickListener.onTrackClick(playlistItem) }
    }

    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: Playlist)
    }
}
