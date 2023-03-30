package com.sakal.playlistmaker

import androidx.recyclerview.widget.DiffUtil
import com.sakal.playlistmaker.model.Track

class TracksDiffCallback(
    private val oldTrackList: ArrayList<Track>,
    private val newTrackList: ArrayList<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldTrackList.size
    }

    override fun getNewListSize(): Int {
        return newTrackList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldTrackList[oldItemPosition]
        val newTrack = newTrackList[newItemPosition]
        return oldTrack.trackId == newTrack.trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldTrack = oldTrackList[oldItemPosition]
        val newTrack = newTrackList[newItemPosition]
        return oldTrack == newTrack
    }


}
