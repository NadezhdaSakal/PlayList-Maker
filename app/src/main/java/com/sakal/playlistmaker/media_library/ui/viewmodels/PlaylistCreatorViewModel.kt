package com.sakal.playlistmaker.media_library.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.domain.PlaylistsInteractor
import com.sakal.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class PlaylistCreatorViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    var isClickable = true
    private val clickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY_MILLIS, viewModelScope, false) {
            isClickable = it
        }

    fun onBtnClick() {
        isClickable = false
        clickDebounce(true)
    }


    fun createPlaylist(
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            playlistsInteractor.createPlaylist(name, description, imageUri)
            onResultListener()
        }
    }

    fun updatePlaylist(
        playListId: Int,
        name: String,
        description: String,
        imageUri: Uri?,
        onResultListener: () -> Unit
    ) {
        viewModelScope.launch {
            playlistsInteractor.updatePlaylist(playListId, name, description, imageUri)
            onResultListener()
        }
    }


}