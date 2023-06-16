package com.sakal.playlistmaker.search.data.network

interface NetworkClient {
    fun doRequest(dto: Any): Response
}