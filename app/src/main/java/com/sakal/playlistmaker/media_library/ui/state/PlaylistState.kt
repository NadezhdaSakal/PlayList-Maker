package com.sakal.playlistmaker.media_library.ui.state

import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track

sealed interface PlaylistState {

    data class PlaylistInfo(
        val playlist: Playlist
    ) : PlaylistState

    data class PlaylistTracks(
        val tracks: List<Track>
    ) : PlaylistState

}
