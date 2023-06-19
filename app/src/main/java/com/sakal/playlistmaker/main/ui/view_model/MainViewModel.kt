package com.sakal.playlistmaker.main.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sakal.playlistmaker.main.ui.NavigationViewState
import com.sakal.playlistmaker.utils.SingleLiveEvent

class MainViewModel : ViewModel() {

    private val navigationViewStateLiveData = SingleLiveEvent<NavigationViewState>()

    fun observeNavigationViewState(): LiveData<NavigationViewState> = navigationViewStateLiveData

    fun searchView() {
        navigationViewStateLiveData.postValue(NavigationViewState.Search)
    }

    fun mediaLibraryView() {
        navigationViewStateLiveData.postValue(NavigationViewState.MediaLibrary)
    }

    fun settingsView() {
        navigationViewStateLiveData.postValue(NavigationViewState.Settings)
    }
}