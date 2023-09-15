package com.sakal.playlistmaker.media_library.domain.impl

import android.net.Uri
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.domain.PlaylistsRepo
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepo,
) : PlaylistsInteractor {

    override suspend fun createPlaylist(playlistName: String, playlistDescription: String, imageUri: Uri?) =
        repository.createPlaylist(playlistName, playlistDescription, imageUri)

    override suspend fun addTrack(track: Track, playlistId: Int) =
        repository.addTrack(track, playlistId)

    override suspend fun isTrackAlreadyExists(trackId : Int, playlistId: Int) : Boolean=
            repository.isTrackAlreadyExists(trackId, playlistId)

    override suspend fun getPlaylists(): List<Playlist> =
        repository.getPlaylists()

    override suspend fun getPlaylist(playlistId: Int) : Playlist =
        repository.getPlaylist(playlistId)


    override suspend fun getPlaylistTracks(playlistId: Int): List<Track> =
        repository.getPlaylistTracks(playlistId)

    override suspend fun updatePlaylist(playlistId: Int, playlistName: String, playlistDescription: String, imageUri: Uri?) {
        repository.updatePlaylist(playlistId, playlistName, playlistDescription, imageUri)
    }

    override suspend fun deleteTrack(trackId: Int, playlistId: Int) =
        repository.deleteTrack(trackId, playlistId)

    override suspend fun deletePlaylist(playlist: Playlist) =
        repository.deletePlaylist(playlist)

}