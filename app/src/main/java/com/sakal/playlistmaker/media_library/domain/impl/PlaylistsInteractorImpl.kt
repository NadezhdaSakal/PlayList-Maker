package com.sakal.playlistmaker.media_library.domain.impl

import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.domain.PlaylistsRepo
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepo,
) : PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getSavedPlaylists()
    }

    override fun isTrackAlreadyExists(playlist: Playlist, track: Track) =
        playlist.trackList.contains(track)

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        var updatedPlaylist: Playlist =
            playlist.copy(trackList = listOf(track) + playlist.trackList)
        updatedPlaylist = updatedPlaylist.copy(tracksCount = updatedPlaylist.trackList.size)

        repository.updateTracks(updatedPlaylist)
    }
}