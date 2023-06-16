package com.sakal.playlistmaker.search.data.network

import com.google.gson.annotations.SerializedName
import com.sakal.playlistmaker.search.data.Response
import com.sakal.playlistmaker.search.data.model.TrackDto

class TracksSearchResponse(@SerializedName("results") val results: ArrayList<TrackDto>): Response()