package com.sakal.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sakal.playlistmaker.media_library.data.db.dao.TracksDao
import com.sakal.playlistmaker.media_library.data.db.entity.TrackEntity
import com.sakal.playlistmaker.media_library.data.db.entity.TypeConverter

@Database(version = 1, entities = [TrackEntity::class])
@TypeConverters(TypeConverter::class)
abstract class DataBase : RoomDatabase() {

    abstract fun tracksDao(): TracksDao
}