package com.sakal.playlistmaker.media_library.domain

import android.net.Uri
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track

interface PlaylistsInteractor {
    suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?)

    suspend fun addTrack(track: Track, playlistId: Int)

    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean

    suspend fun deleteTrack(trackId: Int, playlistId: Int)

    suspend fun getPlaylist(playlistId: Int): Playlist

    suspend fun getPlaylistTracks(playlistId: Int): List<Track>

    suspend fun getPlaylists(): List<Playlist>

    suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri?)

    suspend fun deletePlaylist(playlist: Playlist)

}

