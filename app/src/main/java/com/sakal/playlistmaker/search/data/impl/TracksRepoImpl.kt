package com.sakal.playlistmaker.search.data.impl

import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.search.data.network.NetworkClient
import com.sakal.playlistmaker.search.data.network.TracksSearchRequest
import com.sakal.playlistmaker.search.data.network.TracksSearchResponse
import com.sakal.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksRepo
import com.sakal.playlistmaker.utils.Resource


class TracksRepoImpl(
    private val networkClient: NetworkClient, private val localStorage:
    SharedPreferencesSearchHistoryStorage
) : TracksRepo {

    override fun searchTracks(query: String): Resource<ArrayList<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(query))

        when (response.resultCode) {
            ApiConstants.NO_INTERNET_CONNECTION_CODE -> {
                return Resource.Error(ApiConstants.INTERNET_CONNECTION_ERROR)
            }
            ApiConstants.SUCCESS_CODE -> {
                val arrayListTracks = arrayListOf<Track>()
                (response as TracksSearchResponse).results.forEach {
                    arrayListTracks.add(
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                        )
                    )
                }
                return Resource.Success(arrayListTracks)
            }
            else -> {
                return Resource.Error(ApiConstants.SERVER_ERROR)
            }
        }

    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): ArrayList<Track> {
        return localStorage.getHistory()
    }
}
