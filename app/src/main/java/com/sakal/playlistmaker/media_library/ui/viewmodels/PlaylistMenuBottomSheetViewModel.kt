package com.sakal.playlistmaker.media_library.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistMenuBottomSheetViewModel(
    private val playListsInteractor: PlaylistsInteractor
): ViewModel() {

    var isClickable = true

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

    fun deletePlaylist(playlist: Playlist, onResultListener: () -> Unit) {
        viewModelScope.launch {
            playListsInteractor.deletePlaylist(playlist)
            onResultListener()
        }
    }


}