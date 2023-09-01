package com.sakal.playlistmaker.media_library.ui.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sakal.playlistmaker.media_library.ui.adapters.PlaylistsAdapter
import com.sakal.playlistmaker.media_library.ui.playlist.PlaylistFragment
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsScreenState
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsViewModel
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter {
        clickOnPlaylist(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_newPlaylistFragment
            )
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Content -> showContent(state.playlists)
            PlaylistsScreenState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            placeholderNoPlaylist.visibility = View.VISIBLE
            recyclerViewPlaylist.visibility = View.GONE
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(content: List<Playlist>) {

        binding.apply {
            placeholderNoPlaylist.visibility = View.GONE
            recyclerViewPlaylist.visibility = View.VISIBLE
        }

        playlistsAdapter.apply {
            playlists.clear()
            playlists.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylist.adapter = playlistsAdapter
        binding.recyclerViewPlaylist.addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClick()
        findNavController().navigate(R.id.action_libraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlist.id))

    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}
