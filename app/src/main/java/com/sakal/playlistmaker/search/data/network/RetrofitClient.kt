package com.sakal.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sakal.playlistmaker.ApiConstants


class RetrofitClient(private val service: ApiService, private val context: Context): NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConnected()){
            return Response()
                .apply { resultCode = ApiConstants.NO_INTERNET_CONNECTION_CODE }
        }
        return if (dto is TracksSearchRequest) {
            val resp = service.search(dto.query).execute()
            val body = resp.body() ?: Response()
            body.apply { resultCode = resp.code() }
        }else{
            Response().apply { resultCode = ApiConstants.BAD_REQUEST_CODE }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }


}