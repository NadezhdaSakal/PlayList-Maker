package com.sakal.playlistmaker.media_library.domain

import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    fun isTrackAlreadyExists(playlist: Playlist, track: Track): Boolean
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
}