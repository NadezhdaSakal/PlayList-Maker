package com.sakal.playlistmaker.data

interface MediaPlayerListener {
    fun setStatePrepared()
    fun removeHandlersCallbacks()
    fun setImagePlay()
    fun setCurrentTimeZero()

}