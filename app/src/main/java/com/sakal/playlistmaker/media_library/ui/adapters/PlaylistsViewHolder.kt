package com.sakal.playlistmaker.media_library.ui.adapters

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import java.io.File

class PlaylistsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistCover: ImageView = itemView.findViewById(R.id.cover_playlist)
    private val playlistName: TextView = itemView.findViewById(R.id.name_playlist)
    private val tracksCount: TextView = itemView.findViewById(R.id.count_tracks)

    fun bind(playlist: Playlist) {
        playlistName.text = playlist.name
        tracksCount.text = tracksCount.resources.getQuantityString(
            R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
        )

        val cornerRadius =
            itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)


        val filePath = File(
            itemView.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constants.PLAYLISTS_IMAGES
        )
        Glide
            .with(itemView)
            .load(playlist.cover?.let { imageName -> File(filePath, imageName) })
            .placeholder(R.drawable.placeholder_512)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .into(playlistCover)

    }

}
