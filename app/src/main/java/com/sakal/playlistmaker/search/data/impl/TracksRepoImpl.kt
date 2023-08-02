package com.sakal.playlistmaker.search.data.impl

import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.search.data.network.NetworkClient
import com.sakal.playlistmaker.search.data.network.TracksSearchRequest
import com.sakal.playlistmaker.search.data.network.TracksSearchResponse
import com.sakal.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.domain.TracksRepo
import com.sakal.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class TracksRepoImpl(
    private val networkClient: NetworkClient, private val localStorage:
    SharedPreferencesSearchHistoryStorage
) : TracksRepo {

    override fun searchTracks(query: String): Flow<Resource<ArrayList<Track>>> = flow {

        val response = networkClient.doRequest(TracksSearchRequest(query))

        when (response.resultCode) {
            ApiConstants.NO_INTERNET_CONNECTION_CODE -> {
                emit(Resource.Error(response.resultCode))
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
                emit(Resource.Success(arrayListTracks))
            }
            else -> {
                emit(Resource.Error(response.resultCode))
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
