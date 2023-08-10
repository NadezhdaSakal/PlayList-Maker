package com.sakal.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sakal.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackEntity)

    @Query("DELETE FROM favorites_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Int)

    @Query("SELECT * FROM favorites_tracks ORDER BY saveDate DESC;")
    suspend fun getFavoriteTracks(): List<TrackEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites_tracks WHERE trackId = :trackId)")
    suspend fun isFavorite(trackId: Int): Boolean
}