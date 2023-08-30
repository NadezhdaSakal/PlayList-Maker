package com.sakal.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sakal.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.sakal.playlistmaker.media_library.data.db.dao.TracksDao
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.sakal.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class DataBase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao

    abstract fun playlistsDao(): PlaylistDao

}