package com.sakal.playlistmaker.media_library.ui.viewmodels

import com.sakal.playlistmaker.new_playlist.domain.models.Playlist

sealed class PlaylistsScreenState {

    object Empty : PlaylistsScreenState()

    class Content(val playlists: List<Playlist>) : PlaylistsScreenState()
}
