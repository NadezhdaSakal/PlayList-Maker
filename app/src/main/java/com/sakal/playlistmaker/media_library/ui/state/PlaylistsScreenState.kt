package com.sakal.playlistmaker.media_library.ui.state

import com.sakal.playlistmaker.media_library.domain.models.Playlist

sealed interface PlaylistsScreenState {

    object Empty : PlaylistsScreenState

    data class NotEmpty(
        val playlists: List<Playlist>
    ) : PlaylistsScreenState

}