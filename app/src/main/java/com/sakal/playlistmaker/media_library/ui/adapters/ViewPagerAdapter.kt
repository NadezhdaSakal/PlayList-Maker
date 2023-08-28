package com.sakal.playlistmaker.media_library.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.media_library.ui.favorites.FavoritesFragment
import com.sakal.playlistmaker.media_library.ui.playlists.PlaylistsFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            Constants.INDEX_FIRST -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }

}