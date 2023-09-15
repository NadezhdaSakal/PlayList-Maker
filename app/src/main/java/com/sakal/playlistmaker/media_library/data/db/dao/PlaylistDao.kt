package com.sakal.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistWithCountTracks
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistsTrackEntity
import com.sakal.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(entity = PlaylistsTrackEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylistsTrack(playlistsTrackEntity: PlaylistsTrackEntity)

    @Insert(entity = TrackPlaylistEntity::class)
    suspend fun addTrackPlaylist(trackPlaylistEntity: TrackPlaylistEntity)

    @Transaction
    suspend fun addTrack(
        playlistsTrackEntity: PlaylistsTrackEntity,
        trackPlaylistEntity: TrackPlaylistEntity
    ) {
        addPlaylistsTrack(playlistsTrackEntity)
        addTrackPlaylist(trackPlaylistEntity)
    }

    @Query("SELECT EXISTS (SELECT 1 FROM playlists_track  WHERE trackId = :trackId AND playlistId = :playlistId)")
    suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean

    @Query("DELETE FROM playlists_track WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrackFromTrackPlayList(playlistId: Int, trackId: Int)

    @Transaction
    suspend fun deleteTrack(
        trackId: Int,
        playlistId: Int
    ) {
        deleteTrackFromTrackPlayList(playlistId, trackId)
        clearTracks()
    }

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromPlaylist(playlistId: Int)

    @Query("DELETE FROM playlists_track WHERE playlistId = :playlistId")
    suspend fun deletePlaylistFromTrackPlaylist(playlistId: Int)

    @Query("DELETE FROM track_playlists WHERE trackId NOT IN (SELECT DISTINCT(trackId) FROM playlists_track)")
    suspend fun clearTracks()

    @Transaction
    suspend fun deletePlaylist(playlistId: Int) {
        deletePlaylistFromPlaylist(playlistId)
        deletePlaylistFromTrackPlaylist(playlistId)
        clearTracks()
    }

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT playlistId, name, description, cover, (SELECT COUNT(id) FROM playlists_track WHERE playlists_track.playlistId=playlists.playlistId) as tracksCount FROM playlists ORDER BY playlistId DESC")
    suspend fun getPlaylists(): List<PlaylistWithCountTracks>

    @Query("SELECT playlistId, name, description, cover, (SELECT COUNT(id) FROM playlists_track WHERE playlists_track.playListId=playlists.playlistId) as tracksCount FROM playlists WHERE playlistId = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistWithCountTracks

    @Query("SELECT track_playlists.* FROM track_playlists LEFT JOIN playlists_track ON track_playlists.trackId=playlists_track.trackId WHERE playlists_track.playListId = :playlistId  ORDER BY playlists_track.id DESC")
    suspend fun getPlaylistTracks(playlistId: Int): List<PlaylistsTrackEntity>


}