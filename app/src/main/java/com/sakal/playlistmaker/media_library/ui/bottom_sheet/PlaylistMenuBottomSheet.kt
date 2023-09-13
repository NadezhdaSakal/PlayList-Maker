package com.sakal.playlistmaker.media_library.ui.bottom_sheet

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.BottomSheetPlaylistMenuBinding
import com.sakal.playlistmaker.media_library.domain.models.Playlist
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistMenuBottomSheetViewModel
import com.sakal.playlistmaker.utils.shareText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class PlaylistMenuBottomSheet(private val playlist: Playlist, private val shareText: String) :
    BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetPlaylistMenuBinding

    private val viewModel by viewModel<PlaylistMenuBottomSheetViewModel>()

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetPlaylistMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drawPlaylistInfo()
        initBtnShare()
        initBtnEdit()
        initBtnDelete()
    }

    private fun drawPlaylistInfo() {
        val filePath = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            Constants.PLAYLISTS_IMAGES
        )
        Glide
            .with(binding.itemBottomSheet.namePlaylist)
            .load(playlist.cover?.let { File(filePath, it) })
            .placeholder(R.drawable.placeholder_512)
            .into(binding.itemBottomSheet.coverPlaylist)

        with(binding) {

            itemBottomSheet.namePlaylist.text = playlist.name
            itemBottomSheet.countTracks.text =
                itemBottomSheet.countTracks.resources.getQuantityString(
                    R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
                )

            itemBottomSheet.namePlaylist.text = playlist.name
            itemBottomSheet.countTracks.text =
                itemBottomSheet.countTracks.resources.getQuantityString(
                    R.plurals.tracks, playlist.tracksCount, playlist.tracksCount
                )
        }
    }

    private fun initBtnShare() {
        binding.buttonSharing.setOnClickListener {
            if (viewModel.clickDebounce()) {
                if (playlist.tracksCount > 0) {
                    shareText(shareText, requireContext())
                } else {
                    dismiss()
                    Toast.makeText(
                        requireContext(), getString(R.string.empty_track_list),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun initBtnEdit() {
        binding.buttonEdit.setOnClickListener {
            dismiss()
            findNavController().navigate(
                R.id.action_to_playlist_creator,
                Bundle().apply {
                    putSerializable(Constants.PLAYLIST, playlist)
                }
            )
        }
    }

    private fun initBtnDelete() {
        binding.buttonDelete.setOnClickListener {
            confirmDialog = MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(getString(R.string.delete_playlist_message) + " ${playlist.name}?")
                setNegativeButton(resources.getText(R.string.no)) { _, _ ->
                }
                setPositiveButton(resources.getText(R.string.yes)) { _, _ ->
                    viewModel.deletePlaylist(
                        playlist
                    ) { findNavController().popBackStack() }
                }
            }
            confirmDialog.show()
        }
    }

    companion object {
        const val TAG = "PlaylistBottomSheet"

        fun newInstance(playlist: Playlist, shareText: String): PlaylistMenuBottomSheet {
            return PlaylistMenuBottomSheet(playlist, shareText)
        }
    }
}
