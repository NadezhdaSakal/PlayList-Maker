package com.sakal.playlistmaker.new_playlist.domain.models

import com.sakal.playlistmaker.search.domain.Track

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    val trackList: List<Track>,
    val tracksCount: Int,
)