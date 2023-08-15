package com.sakal.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sakal.playlistmaker.media_library.data.db.dao.TracksDao
import com.sakal.playlistmaker.media_library.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class DataBase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao
}