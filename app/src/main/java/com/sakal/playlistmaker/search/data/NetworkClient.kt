package com.sakal.playlistmaker.search.data

interface NetworkClient {
    fun doRequest(dto: Any): Response
}