package com.sakal.playlistmaker.media_library.ui.fragments.playlists

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.media_library.ui.adapters.PlaylistsAdapter
import com.sakal.playlistmaker.media_library.ui.adapters.PlaylistsViewHolder
import com.sakal.playlistmaker.media_library.ui.state.PlaylistsScreenState
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel by viewModel<PlaylistsViewModel>()

    private val playlistsAdapter = object : PlaylistsAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
            return PlaylistsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_playlist, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsScreenState.Empty -> {
                    binding.recyclerViewPlaylists.visibility = View.GONE
                    binding.placeholderNoPlaylist.visibility = View.VISIBLE
                }

                is PlaylistsScreenState.NotEmpty -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists
                    binding.placeholderNoPlaylist.visibility = View.GONE
                    binding.recyclerViewPlaylists.visibility = View.VISIBLE
                    binding.recyclerViewPlaylists.smoothScrollToPosition(0)
                }
            }
        }

        initAdapter()

        initBtnNewPlaylist()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylists.adapter = playlistsAdapter
    }

    private fun initBtnNewPlaylist() {
        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_playlist_creator
            )
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.action_to_PlaylistFragment,
            Bundle().apply {
                putSerializable(Constants.PLAYLIST, playlist)
            }
        )
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}