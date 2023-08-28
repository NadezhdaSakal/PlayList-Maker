package com.sakal.playlistmaker.media_library.domain

import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepo {
    suspend fun createPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateTracks(playlist: Playlist)

    fun getSavedPlaylists(): Flow<List<Playlist>>
}