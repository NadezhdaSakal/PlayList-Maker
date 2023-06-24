package com.sakal.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.sakal.playlistmaker.databinding.FragmentPlaylistsBinding
import com.sakal.playlistmaker.media_library.ui.viewmodels.PlaylistsFragmentViewModel

class PlaylistsFragment : Fragment() {

    private val viewModel by viewModel<PlaylistsFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}