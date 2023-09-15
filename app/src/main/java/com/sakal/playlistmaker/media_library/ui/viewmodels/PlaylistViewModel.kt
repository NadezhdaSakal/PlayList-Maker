package com.sakal.playlistmaker.media_library.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.ui.state.PlaylistState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistState>()

    var isClickable = true

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    private fun renderState(state: PlaylistState) {
        stateLiveData.postValue(state)
    }

    fun requestPlayListInfo(playlistId: Int) {
        viewModelScope.launch {
            renderState(
                PlaylistState.PlaylistInfo(
                    playlistsInteractor.getPlaylist(playlistId)
                )
            )
            renderState(
                PlaylistState.PlaylistTracks(
                    playlistsInteractor.getPlaylistTracks(playlistId)
                )
            )
        }
    }

    fun deleteTrack(trackId: Int, playlistId: Int) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrack(trackId, playlistId)
            renderState(
                PlaylistState.PlaylistTracks(
                    playlistsInteractor.getPlaylistTracks(playlistId)
                )
            )
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickable
        if (isClickable) {
            isClickable = false
            viewModelScope.launch {
                delay(Constants.CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickable = true
            }
        }
        return current
    }

}









