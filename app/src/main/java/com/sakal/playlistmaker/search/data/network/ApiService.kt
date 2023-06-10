package com.sakal.playlistmaker.search.data.network

import com.sakal.playlistmaker.search.data.dto.ITResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITResponse>
}