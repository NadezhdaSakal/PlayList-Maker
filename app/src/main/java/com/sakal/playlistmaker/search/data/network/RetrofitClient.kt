package com.sakal.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.BuildConfig
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.data.NetworkClient
import com.sakal.playlistmaker.search.data.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.sakal.playlistmaker.search.data.TracksSearchRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit


class RetrofitClient(private val context: Context): NetworkClient {

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(Constants.CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val service = retrofit.create(ApiService::class.java)

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