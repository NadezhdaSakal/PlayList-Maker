package com.sakal.playlistmaker.domain

interface PlayerStateListener {
    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setImagePlay()
    fun setCurrentTimeZero()
}