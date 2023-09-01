package com.sakal.playlistmaker.media_library.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentPlaylistBinding
import com.sakal.playlistmaker.media_library.ui.bottom_sheet.BottomSheetSetuper
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistViewModel
import com.sakal.playlistmaker.new_playlist.domain.models.Playlist
import com.sakal.playlistmaker.utils.setImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModel<PlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root

        initPlaylist()

        initToolbar()

        initBottomSheetBehavior()

    }

    private fun initPlaylist() {
        val playlistId: Int = requireArguments().getInt(Constants.PLAYLIST_ID)

    }

    private fun initToolbar() {
        binding.playlistToolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showPlaylist(playlist: Playlist) {
        with(binding) {

            ivPlaylistCover.setImage(
                url = playlist.coverImageUrl,
                placeholder = R.drawable.placeholder_512,
            )

            tvPlaylistName.text = playlist.playlistName
            tvPlaylistDescription.text = playlist.playlistDescription

            if (playlist.playlistDescription.isEmpty()) {
                tvPlaylistDescription.visibility = View.GONE
            }
        }
    }

    private fun initBottomSheetBehavior() {
        val bottomSheetSetuper = BottomSheetSetuper(activity)
        bottomSheetSetuper.setupRatio(
            container = binding.bottomSheetTrackList, percentage = PERCENT_OCCUPIED_BY_BOTTOM_SHEET
        )
    }

    companion object {

        private const val PERCENT_OCCUPIED_BY_BOTTOM_SHEET = 0.45f

        fun createArgs(id: Int): Bundle = bundleOf(
            Constants.PLAYLIST_ID to id
        )    }



}