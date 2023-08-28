package com.sakal.playlistmaker.media_library.data.impl

import com.sakal.playlistmaker.media_library.data.db.DataBase
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.sakal.playlistmaker.media_library.data.db.entity.RoomConverter
import com.sakal.playlistmaker.media_library.domain.PlaylistsRepo
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PlaylistsRepoImpl(
    private val database: DataBase,
    private val converter: RoomConverter,
) : PlaylistsRepo {

    override suspend fun createPlaylist(playlist: Playlist) {
        database
            .playlistsDao()
            .insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database
            .playlistsDao()
            .deletePlaylist(converter.map(playlist))
    }

    override suspend fun updateTracks(playlist: Playlist) {
        database
            .playlistsDao()
            .updatePlaylist(converter.map(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return database
            .playlistsDao()
            .getSavedPlaylists()
            .map { convertFromTrackEntity(it) }
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { converter.map(it) }
    }
}
