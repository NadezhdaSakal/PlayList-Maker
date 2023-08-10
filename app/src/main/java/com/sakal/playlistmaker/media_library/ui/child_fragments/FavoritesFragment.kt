package com.sakal.playlistmaker.media_library.ui.child_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.databinding.FragmentFavoritesBinding
import com.sakal.playlistmaker.media_library.ui.viewmodels.FavoritesFragmentViewModel
import com.sakal.playlistmaker.media_library.ui.viewmodels.FavoritesState
import com.sakal.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.adapters.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModel<FavoritesFragmentViewModel>()
    private val tracksAdapter = TrackAdapter {
        clickOnTrack(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeContentState().observe(viewLifecycleOwner) {
            render(it)
        }

        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoritesTracks()
    }


    private fun clickOnTrack(track: Track) {
        if (!viewModel.isClickable) return
        viewModel.onTrackClick()
        val intent = Intent(requireContext(), AudioPlayerActivity::class.java).apply {
            putExtra(Constants.TRACK, track)
        }
        startActivity(intent)
    }

    private fun initAdapter() {
        binding.favoritesList.adapter = tracksAdapter
        tracksAdapter.notifyDataSetChanged()
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> {
                binding.placeholderNothingWasFound.visibility = View.VISIBLE
                binding.favoritesList.visibility = View.GONE
            }

            is FavoritesState.FavoritesTracks -> {
                tracksAdapter.tracks = state.tracks
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.favoritesList.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }

}





