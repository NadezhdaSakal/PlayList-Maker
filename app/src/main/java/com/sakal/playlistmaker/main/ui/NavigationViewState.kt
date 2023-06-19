package com.sakal.playlistmaker.main.ui

sealed interface NavigationViewState {
    object Search: NavigationViewState
    object MediaLibrary: NavigationViewState
    object Settings: NavigationViewState
}