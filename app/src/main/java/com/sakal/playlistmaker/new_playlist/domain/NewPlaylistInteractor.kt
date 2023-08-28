package com.sakal.playlistmaker.new_playlist.domain

import com.sakal.playlistmaker.new_playlist.domain.models.Playlist

interface NewPlaylistInteractor {

    suspend fun create(playlist: Playlist)

}