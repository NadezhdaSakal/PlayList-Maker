package com.sakal.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.BuildConfig
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.data.Player
import com.sakal.playlistmaker.player.data.PlayerClient
import com.sakal.playlistmaker.search.data.preferences.SearchHistorySrorage
import com.sakal.playlistmaker.search.data.network.NetworkClient
import com.sakal.playlistmaker.search.data.network.ApiService
import com.sakal.playlistmaker.search.data.network.RetrofitClient
import com.sakal.playlistmaker.search.data.preferences.SharedPreferencesSearchHistoryStorage
import com.sakal.playlistmaker.settings.data.preferences.ThemeStorage
import com.sakal.playlistmaker.settings.data.preferences.SharedPrefsThemeStorage
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {

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

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        retrofit.create(ApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(
                Constants.HISTORY_TRACKS_SHARED_PREF,
                Context.MODE_PRIVATE)
    }

    factory { Gson() }

    singleOf(::SharedPreferencesSearchHistoryStorage).bind<SearchHistorySrorage>()

    singleOf(:: RetrofitClient).bind<NetworkClient>()

    factory<PlayerClient> {
        Player(client = get())
    }

    factory {
        MediaPlayer()
    }

    singleOf(::SharedPrefsThemeStorage).bind<ThemeStorage>()

    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            Constants.PLAYLIST_MAKER_PREFS,
            Context.MODE_PRIVATE
        )
    }
}