package com.sakal.playlistmaker.main.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sakal.playlistmaker.databinding.ActivityMainBinding
import com.sakal.playlistmaker.main.ui.NavigationViewState
import com.sakal.playlistmaker.main.ui.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModel()
    private val router = NavigationRouter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.observeNavigationViewState().observe(this) {
            navigation(it)
        }

        initButtonSearch()

        initButtonMedia()

        initButtonSettings()

    }

    private fun navigation(state: NavigationViewState) {
        when (state) {
            is NavigationViewState.Search -> router.toSearch()
            is NavigationViewState.Settings -> router.toSettings()
            is NavigationViewState.MediaLibrary -> router.toMediaLibrary()
        }
    }

    private fun initButtonSearch() {
        binding.btnSearch.setOnClickListener() {
            mainViewModel.searchView()
        }
    }

    private fun initButtonMedia() {
        binding.btnMedia.setOnClickListener() {
            mainViewModel.mediaLibraryView()
        }
    }

    private fun initButtonSettings() {
        binding.btnSettings.setOnClickListener() {
            mainViewModel.settingsView()
        }
    }

}
