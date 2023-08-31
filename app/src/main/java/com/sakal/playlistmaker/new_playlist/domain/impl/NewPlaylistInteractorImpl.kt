package com.sakal.playlistmaker.new_playlist.domain.impl

import com.sakal.playlistmaker.media_library.domain.PlaylistsRepo
import com.sakal.playlistmaker.new_playlist.domain.NewPlaylistInteractor
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist

class NewPlaylistInteractorImpl (
    private val repository: PlaylistsRepo,
) : NewPlaylistInteractor {

    override suspend fun create(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}