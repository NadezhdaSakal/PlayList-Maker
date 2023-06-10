package com.sakal.playlistmaker.search.domain

interface TracksLoadResultListener {
    fun onSuccess(tracks: List<Track>)
    fun onError()
}