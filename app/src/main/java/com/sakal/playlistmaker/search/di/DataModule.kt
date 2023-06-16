package com.sakal.playlistmaker.search.di

import android.content.Context
import com.google.gson.Gson
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.BuildConfig
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.search.data.ILocalStorage
import com.sakal.playlistmaker.search.data.NetworkClient
import com.sakal.playlistmaker.search.data.network.ApiService
import com.sakal.playlistmaker.search.data.network.RetrofitClient
import com.sakal.playlistmaker.search.data.storage.LocalStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val searchDataModule = module {

    single<ApiService> {

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(Constants.CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()


        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<ILocalStorage> {
        LocalStorage(
            preferences = get(),
            gson = get()
        )
    }

    single<NetworkClient> {
        RetrofitClient(
            service = get(),
            context = androidContext()
        )
    }
}