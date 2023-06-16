package com.sakal.playlistmaker.search.di

import com.sakal.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchViewModelModule = module {

    viewModel {
        SearchViewModel(tracksInteractor = get())
    }
}