package com.sakal.playlistmaker.media_library.ui.state

import com.sakal.playlistmaker.media_library.domain.models.Playlist


sealed interface PlaylistsState {

    object Empty : PlaylistsState

    data class Playlists(
        val playlists: List<Playlist>
    ) : PlaylistsState

    data class AddTrackResult(
        val isAdded: Boolean,
        val playlistName: String
    ) : PlaylistsState

}