package com.sakal.playlistmaker.search.di

import com.sakal.playlistmaker.search.domain.TracksInteractor
import com.sakal.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchInteractorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }
}