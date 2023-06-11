package com.sakal.playlistmaker.search.data.impl

import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.search.data.dto.TrackDto
import com.sakal.playlistmaker.search.domain.TracksLoadResultListener
import com.sakal.playlistmaker.search.data.dto.ITResponse
import com.sakal.playlistmaker.search.data.network.ApiService
import com.sakal.playlistmaker.search.domain.SearchRepo
import com.sakal.playlistmaker.search.domain.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepoImpl(private val api: ApiService) : SearchRepo {

    override var tracksLoadResultListener: TracksLoadResultListener? = null

    override fun loadTracks(query: String) {
        api.search(query)
            .enqueue(object : Callback<ITResponse> {
                override fun onResponse(
                    call: Call<ITResponse>,
                    response: Response<ITResponse>,
                ) {
                    if (response.code() == ApiConstants.SUCCESS_CODE) {

                        val tracks =
                            response.body()?.results!!.map { mapTrack(it) }.filter { track ->
                                true
                            }
                        tracksLoadResultListener?.onSuccess(tracks = tracks)

                    }

                }

                override fun onFailure(call: Call<ITResponse>, t: Throwable) {
                    tracksLoadResultListener?.onError()
                }
            })
    }

    private fun mapTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.trackTimeMillis,
            trackDto.artworkUrl100,
            trackDto.trackId,
            trackDto.collectionName,
            trackDto.releaseDate.orEmpty(),
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl.orEmpty()
        )
    }
}