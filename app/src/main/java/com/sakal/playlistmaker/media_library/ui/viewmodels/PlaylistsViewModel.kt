package com.sakal.playlistmaker.media_library.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val interactor: PlaylistsInteractor) :
    ViewModel() {

    private val _contentFlow: MutableStateFlow<PlaylistsScreenState> =
        MutableStateFlow(PlaylistsScreenState.Empty)
    val contentFlow: StateFlow<PlaylistsScreenState> = _contentFlow

    var isClickable = true

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }

    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = (PlaylistsScreenState.Empty)
        } else {
            _contentFlow.value = (PlaylistsScreenState.Content(playlists))
        }
    }

    fun onPlaylistClick() {
        isClickable = false
    }

}