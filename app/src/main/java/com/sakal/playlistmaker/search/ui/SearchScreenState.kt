package com.sakal.playlistmaker.search.ui

import android.view.View
import com.sakal.playlistmaker.databinding.ActivitySearchBinding
import com.sakal.playlistmaker.search.domain.Track

sealed class SearchScreenState( val tracks: List<Track>? = null) {

    class Loading : SearchScreenState() {

        override fun render(binding: ActivitySearchBinding) {
            binding.recyclerViewSearch.visibility = View.GONE
            binding.historyList.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.GONE
            binding.placeholderCommunicationsProblem.visibility = View.GONE
            binding.searchProgressBar.visibility = View.VISIBLE
        }
    }


    class NothingFound : SearchScreenState() {

        override fun render(binding: ActivitySearchBinding) {
            binding.recyclerViewSearch.visibility = View.GONE
            binding.historyList.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.VISIBLE
            binding.placeholderCommunicationsProblem.visibility = View.GONE
            binding.searchProgressBar.visibility = View.GONE
        }
    }

    class Error : SearchScreenState() {

        override fun render(binding: ActivitySearchBinding) {
            binding.recyclerViewSearch.visibility = View.GONE
            binding.historyList.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.GONE
            binding.placeholderCommunicationsProblem.visibility = View.VISIBLE
            binding.searchProgressBar.visibility = View.GONE
        }
    }

    class Success(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {

        override fun render(binding: ActivitySearchBinding) {
            binding.recyclerViewSearch.visibility = View.VISIBLE
            binding.historyList.visibility = View.GONE
            binding.placeholderNothingWasFound.visibility = View.GONE
            binding.placeholderCommunicationsProblem.visibility = View.GONE
            binding.searchProgressBar.visibility = View.GONE
        }
    }

    class ShowHistory(tracks: List<Track>?) : SearchScreenState(tracks = tracks) {

        override fun render(binding: ActivitySearchBinding) {
            binding.recyclerViewSearch.visibility = View.GONE
            binding.historyList.visibility = View.VISIBLE
            binding.placeholderNothingWasFound.visibility = View.GONE
            binding.placeholderCommunicationsProblem.visibility = View.GONE
            binding.searchProgressBar.visibility = View.GONE
        }
    }

    abstract fun render(binding: ActivitySearchBinding)
}
