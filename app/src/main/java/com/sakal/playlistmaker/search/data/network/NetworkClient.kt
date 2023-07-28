package com.sakal.playlistmaker.search.data.network

interface NetworkClient {
    suspend fun doRequest (dto : Any): Response
}