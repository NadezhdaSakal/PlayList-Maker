package com.sakal.playlistmaker.media_library.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.sakal.playlistmaker.databinding.ActivityLibraryBinding
import com.sakal.playlistmaker.media_library.ui.MediaRouter
import com.sakal.playlistmaker.media_library.ui.adapters.ViewPagerAdapter
import com.sakal.playlistmaker.R

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val navigationRouter = MediaRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.favorites_tracks)
                else -> tab.text = resources.getString(R.string.playlists)
            }
        }

        tabMediator.attach()

        initToolbar()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun initToolbar() {
        binding.mediatekaToolbar.setNavigationOnClickListener() {
            navigationRouter.goBack()
        }
    }
}