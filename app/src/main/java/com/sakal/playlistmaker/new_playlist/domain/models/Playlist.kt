package com.sakal.playlistmaker.new_playlist.domain.models

import com.sakal.playlistmaker.search.domain.Track

data class Playlist(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var trackList: List<Track>,
    var tracksCount: Int,
)