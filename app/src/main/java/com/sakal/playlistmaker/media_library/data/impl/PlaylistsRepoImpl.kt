package com.sakal.playlistmaker.media_library.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.data.db.DataBase
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistWithCountTracks
import com.sakal.playlistmaker.media_library.data.db.entity.PlaylistsTrackEntity
import com.sakal.playlistmaker.media_library.data.db.entity.RoomConverter
import com.sakal.playlistmaker.media_library.data.db.entity.TrackPlaylistEntity
import com.sakal.playlistmaker.media_library.domain.PlaylistsRepo
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.search.domain.Track
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar


class PlaylistsRepoImpl(
    private val database: DataBase,
    private val converter: RoomConverter,
    private val context: Context

) : PlaylistsRepo {

    private val filePath = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        Constants.PLAYLISTS_IMAGES
    )

    override suspend fun createPlaylist(
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        var imageFileName: String? = null
        if (imageUri != null) {
            imageFileName = saveAlbumImage(imageUri)
        }
        database
            .playlistsDao()
            .insertPlaylist(
            PlaylistEntity(
                null,
                playlistName,
                playlistDescription,
                imageFileName,
            )
        )
    }

    override suspend fun addTrack(track: Track, playlistId: Int) =
        database
            .playlistsDao()
            .addTrack(
            playlistsTrackEntity = converter.map(track),
            trackPlaylistEntity = TrackPlaylistEntity(null, playlistId, track.trackId)
        )

    override suspend fun isTrackAlreadyExists(trackId: Int, playlistId: Int): Boolean =
        database
            .playlistsDao()
            .isTrackAlreadyExists(trackId, playlistId)


    override suspend fun getPlaylists(): List<Playlist> =
        convertPlaylistWithCountTracksToPlaylist(
            database
                .playlistsDao()
                .getPlaylists()
        )

    override suspend fun getPlaylist(playlistId: Int): Playlist =
        converter.map(
        database
            .playlistsDao()
            .getPlaylist(playlistId)
        )

    override suspend fun getPlaylistTracks(playlistId: Int): List<Track> =
        convertPlaylistsTrackEntityToTrack(
            database
                .playlistsDao()
                .getPlaylistTracks(playlistId)
        )

    override suspend fun deleteTrack(trackId: Int, playlistId: Int) {
        database
            .playlistsDao()
            .deleteTrack(trackId, playlistId)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlist.cover?.let {
            deleteAlbumImage(it)
        }
        database
            .playlistsDao()
            .deletePlaylist(playlist.playlistId)
    }


    override suspend fun updatePlaylist(
        playlistId: Int,
        playlistName: String,
        playlistDescription: String,
        imageUri: Uri?
    ) {
        val playlist = database
            .playlistsDao()
            .getPlaylist(playlistId)

        var imageFileName = playlist.cover
        if (imageUri != null) {
            if (playlist.cover != null) {
                deleteAlbumImage(playlist.cover)
            }
            imageFileName = saveAlbumImage(imageUri)
        }

        database
            .playlistsDao()
            .updatePlaylist(
            PlaylistEntity(
                playlistId,
                playlistName,
                playlistDescription,
                imageFileName
            )
        )
    }

    private fun convertPlaylistsTrackEntityToTrack(tracks: List<PlaylistsTrackEntity>): List<Track> =
        tracks.map {
            converter.map(it)
        }

    private fun convertPlaylistWithCountTracksToPlaylist(playListWithCountTracks: List<PlaylistWithCountTracks>): List<Playlist> =
        playListWithCountTracks.map {
            converter.map(it)
        }

    private fun saveAlbumImage(uri: Uri): String {
        val imageFileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        BitmapFactory
            .decodeStream(
                context.contentResolver.openInputStream(uri)
            )
            .compress(
                Bitmap.CompressFormat.JPEG,
                Constants.QUALITY_IMAGE,
                FileOutputStream(
                    File(filePath, imageFileName)
                )
            )
        return imageFileName
    }

    private fun deleteAlbumImage(imageFileName: String) {
        if (File(filePath, imageFileName).exists()) {
            File(filePath, imageFileName).delete()
        }
    }

}