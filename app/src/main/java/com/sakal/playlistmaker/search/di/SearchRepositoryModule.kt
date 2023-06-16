package com.sakal.playlistmaker.search.di

import com.sakal.playlistmaker.search.data.impl.TracksRepoImpl
import com.sakal.playlistmaker.search.domain.TracksRepo
import org.koin.dsl.module

val searchRepositoryModule = module {

    single<TracksRepo> {
        TracksRepoImpl(networkClient = get(), localStorage = get())
    }
}