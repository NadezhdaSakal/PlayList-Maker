package com.sakal.playlistmaker.search.data.impl

import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.search.data.NetworkClient
import com.sakal.playlistmaker.search.data.TracksSearchRequest
import com.sakal.playlistmaker.search.data.network.TracksSearchResponse
import com.sakal.playlistmaker.search.data.storage.LocalStorage
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksRepo
import com.sakal.playlistmaker.utils.Resource
import com.sakal.playlistmaker.search.data.model.*


class TracksRepoImpl(private val networkClient: NetworkClient, private val localStorage:
LocalStorage) :
    TracksRepo {
    override fun searchTracks(query: String): Resource<List<Track>> {
        val response = networkClient.doRequest(
            TracksSearchRequest(
                query
            )
        )
        return when (response.resultCode) {
            ApiConstants.NO_INTERNET_CONNECTION_CODE -> {
                Resource.Error(message = ApiConstants.INTERNET_CONNECTION_ERROR, code = ApiConstants.NO_INTERNET_CONNECTION_CODE)
            }
            ApiConstants.SUCCESS_CODE -> {
                Resource.Success((response as TracksSearchResponse).results.map {
                    it.mapToTrack()
                }, code = ApiConstants.SUCCESS_CODE)
            }
            else -> {
                Resource.Error(message = ApiConstants.SERVER_ERROR, code = response.resultCode)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return localStorage.getHistory()
    }
}
