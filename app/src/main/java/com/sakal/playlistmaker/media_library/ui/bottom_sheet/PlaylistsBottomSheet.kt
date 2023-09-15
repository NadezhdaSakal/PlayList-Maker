package com.sakal.playlistmaker.media_library.ui.bottom_sheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.BottomSheetPlaylistsBinding
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.media_library.ui.adapters.PlaylistsAdapter
import com.sakal.playlistmaker.media_library.ui.adapters.PlaylistsViewHolder
import com.sakal.playlistmaker.media_library.ui.state.PlaylistsState
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsBottomSheetViewModel
import com.sakal.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsBottomSheet(val track: Track) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistsBinding

    private val viewModel by viewModel<PlaylistsBottomSheetViewModel>()

    private val playlistsAdapter = object : PlaylistsAdapter(
        clickListener = {
            clickOnPlaylist(it)
        }
    ) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
            return PlaylistsViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_bottom_sheet, parent, false)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            when (it) {
                is PlaylistsState.Empty -> binding.playlistsRecycler.visibility = View.GONE

                is PlaylistsState.Playlists -> {
                    playlistsAdapter.notifyDataSetChanged()
                    playlistsAdapter.playlists = it.playlists
                    binding.playlistsRecycler.visibility = View.VISIBLE
                }

                is PlaylistsState.AddTrackResult -> {
                    if (it.isAdded) {
                        showToast(getString(R.string.added, it.playlistName))
                        dismiss()
                    } else {
                        showToast(getString(R.string.already_added, it.playlistName))
                    }
                }
            }
        }

        initBtnCreate()

        initAdapter()
    }

    private fun initAdapter() {
        binding.playlistsRecycler.adapter = playlistsAdapter
    }

    private fun initBtnCreate() {
        binding.createPlaylistBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_to_playlist_creator
            )
        }
    }

    private fun clickOnPlaylist(playlist: Playlist) {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClicked()
        viewModel.addTrackToPlaylist(track, playlist)
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlaylists()
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val TAG = "PlaylistsBottomSheet"

        fun newInstance(track: Track): PlaylistsBottomSheet {
            return PlaylistsBottomSheet(track)
        }
    }
}