package com.sakal.playlistmaker.media_library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.ui.state.PlaylistsScreenState
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsScreenState>()

    fun observeState(): LiveData<PlaylistsScreenState> = stateLiveData

    private fun renderState(state: PlaylistsScreenState) {
        stateLiveData.postValue(state)
    }

    fun requestPlaylists() {
        viewModelScope.launch {
            val playlists = interactor.getPlaylists()

            if (playlists.isEmpty()) {
                renderState(PlaylistsScreenState.Empty)
            } else {
                renderState(PlaylistsScreenState.NotEmpty(playlists))
            }
        }
    }
}