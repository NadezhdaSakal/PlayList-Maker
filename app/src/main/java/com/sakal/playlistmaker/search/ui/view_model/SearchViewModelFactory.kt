package com.sakal.playlistmaker.search.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.search.domain.SearchInteractor

class SearchViewModelFactory(private val interactor: SearchInteractor) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(interactor) as T
    }
}