package com.sakal.playlistmaker

import androidx.recyclerview.widget.DiffUtil
import com.sakal.playlistmaker.model.Track

class TracksDiffCallback(
    private val trackList: ArrayList<Track>,
    private val newTrackList: ArrayList<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return trackList.size
    }

    override fun getNewListSize(): Int {
        return newTrackList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = trackList[oldItemPosition]
        val newTrack = newTrackList[newItemPosition]
        return oldTrack.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = trackList[oldItemPosition]
        val newTrack = newTrackList[newItemPosition]
        return oldTrack == newTrack
    }


}
