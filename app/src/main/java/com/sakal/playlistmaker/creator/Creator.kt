package com.sakal.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.BuildConfig
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.player.data.PlayerRepositoryImpl
import com.sakal.playlistmaker.player.domain.api.PlayerInteractor
import com.sakal.playlistmaker.player.domain.api.PlayerRepository
import com.sakal.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.sakal.playlistmaker.search.data.SearchRepoImpl
import com.sakal.playlistmaker.search.data.network.ApiService
import com.sakal.playlistmaker.search.data.preferences.LocalStorage
import com.sakal.playlistmaker.search.domain.HistoryDataStore
import com.sakal.playlistmaker.search.domain.SearchInteractor
import com.sakal.playlistmaker.search.domain.SearchInteractorImpl
import com.sakal.playlistmaker.search.domain.SearchRepo
import com.sakal.playlistmaker.search.ui.view_model.SearchViewModelFactory
import com.sakal.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.sakal.playlistmaker.settings.data.preferences.ThemeStorage
import com.sakal.playlistmaker.settings.domain.api.SettingsInteractor
import com.sakal.playlistmaker.settings.domain.api.SettingsRepository
import com.sakal.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Creator {

    private fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            getHistoryDataStore(context),
            getSearchRepo()
        )
    }

    private fun getHistorySharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.HISTORY_TRACKS, AppCompatActivity.MODE_PRIVATE)
    }

    private fun getHistoryDataStore(context: Context): HistoryDataStore {
        return LocalStorage(getHistorySharedPreferences(context))
    }

    private fun getSearchRepo(): SearchRepo {
        return SearchRepoImpl(getITunesApi())
    }

    private fun getITunesApi(): ApiService {

        val okHttpClient = OkHttpClient.Builder()
            .callTimeout(Constants.CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BASIC
                }
            })
            .build()


        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
        return SearchViewModelFactory(provideSearchInteractor(context))

    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    private fun getSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(
            ThemeStorage(context.getSharedPreferences(Constants.PLAYLIST_MAKER_PREFS, Context.MODE_PRIVATE))
        )
    }
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }
}