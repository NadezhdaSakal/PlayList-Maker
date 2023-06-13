package com.sakal.playlistmaker.search.data.model

import com.sakal.playlistmaker.player.domain.TrackForPlayer
import com.sakal.playlistmaker.search.data.storage.TrackToStorage
import com.sakal.playlistmaker.search.domain.Track

fun TrackDto.mapToTrack() = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun TrackToStorage.mapToTrack() = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun Track.mapToTrackToStorage() = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun Track.mapTrackToTrackForPlayer() = TrackForPlayer(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)