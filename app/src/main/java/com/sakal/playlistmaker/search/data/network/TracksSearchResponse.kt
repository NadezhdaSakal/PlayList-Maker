package com.sakal.playlistmaker.search.data.network

import com.sakal.playlistmaker.search.data.model.TrackDto

class TracksSearchResponse(val results: List<TrackDto>) : Response()