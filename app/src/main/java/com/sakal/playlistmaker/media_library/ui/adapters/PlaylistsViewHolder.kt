package com.sakal.playlistmaker.media_library.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.ItemPlaylistBinding
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import com.sakal.playlistmaker.utils.setImage

class PlaylistsViewHolder(
    private val binding: ItemPlaylistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)

        binding.namePlaylist.text = model.playlistName
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)

        binding.coverPlaylist.setImage(
            url = model.coverImageUrl,
            placeholder = R.drawable.placeholder_512,
            cornerRadius = cornerRadius,
        )
    }
}