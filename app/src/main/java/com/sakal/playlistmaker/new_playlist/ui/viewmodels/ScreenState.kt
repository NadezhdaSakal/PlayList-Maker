package com.sakal.playlistmaker.new_playlist.ui.viewmodels

sealed class ScreenState(
    val createBtnState: BtnCreateState = BtnCreateState.ENABLED
) {
    class Empty(
        createBtnState: BtnCreateState = BtnCreateState.DISABLED,
    ) : ScreenState(createBtnState)

    class HasContent(
        createBtnState: BtnCreateState = BtnCreateState.ENABLED,
    ) : ScreenState(createBtnState)

    object NeedsToAsk : ScreenState()

    object AllowedToGoOut : ScreenState()
}