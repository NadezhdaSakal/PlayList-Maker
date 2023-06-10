package com.sakal.playlistmaker.search.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.creator.Creator
import com.sakal.playlistmaker.databinding.ActivitySearchBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.Content
import com.sakal.playlistmaker.search.ui.Router
import com.sakal.playlistmaker.search.ui.SearchState
import com.sakal.playlistmaker.search.ui.adapters.TrackRecyclerAdapter
import com.sakal.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var router: Router

    private val adapterSearch = TrackRecyclerAdapter {
        clickOnTrack(it)
    }

    private val adapterHistory = TrackRecyclerAdapter {
        clickOnTrack(it)
    }

    private var textSearch = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { loadTracks() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)

        setContentView(binding.root)


        router = Router(this)
        viewModel =
            ViewModelProvider(
                this,
                Creator.provideSearchViewModelFactory(this)
            )[SearchViewModel::class.java]

        viewModel.state.observe(this) {
            render(it)
        }

        initToolbar()

        initSearch()

        inputText()

        initSearchResults()

        initHistory()

    }

    private fun render(screenState: SearchState) {
        when (screenState) {
            is SearchState.Tracks -> showTracks(screenState.tracks)
            is SearchState.Error -> showError()
            is SearchState.History -> showHistory(screenState.tracks)
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmptyResult()
            is SearchState.changeTextSearch -> showChangeTextSearch()
        }
    }


    private fun initToolbar() {
        binding.searchToolbar.setNavigationOnClickListener {
            router.goBack()
        }
    }

    private fun showChangeTextSearch() {
        binding.placeholderNothingWasFound.isVisible = false
        binding.placeholderCommunicationsProblem.isVisible = false
        binding.searchProgressBar.visibility = View.GONE
        binding.buttonClearSearchForm.visibility = View.GONE
        binding.buttonClearHistory.isEnabled = true
    }

    private fun initSearch() {
        binding.inputSearchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                true
            }
            false
        }

        binding.inputSearchForm.setOnFocusChangeListener { _, hasFocus ->
            viewModel.searchFocusChanged(hasFocus, binding.inputSearchForm.text.toString())
        }

        binding.inputSearchForm.requestFocus()

        binding.buttonClearSearchForm.setOnClickListener {
            clearSearch()
        }

        binding.buttonRetry.setOnClickListener {
            viewModel.loadTracks(binding.inputSearchForm.text.toString())
        }
    }

    private fun inputText() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.buttonClearSearchForm.visibility = buttonSearchClearVisibility(s)
                showContent(Content.LOADING)
                textSearch =  binding.inputSearchForm.text.toString()

                if (binding.inputSearchForm.hasFocus() && textSearch.isNotEmpty()) {
                    viewModel.searchDebounce()
                    adapterHistory.notifyDataSetChanged()
                } else
                    if (binding.inputSearchForm.hasFocus() && s?.isNotEmpty() == false && viewModel.showHistoryTracks()
                            .isNotEmpty()) {
                        handler.removeCallbacks(searchRunnable)
                        showContent(Content.TRACKS_HISTORY)
                    } else {
                        handler.removeCallbacks(searchRunnable)
                        showContent(Content.SEARCH_RESULT)
                    }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        binding.inputSearchForm.addTextChangedListener(simpleTextWatcher)
    }

    private fun buttonSearchClearVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    private fun initSearchResults() {
        binding.recyclerViewSearch.adapter = adapterSearch
    }

    private fun clickOnTrack(track: Track) {
        if (viewModel.clickDebounce()) {
            viewModel.openTrack(track)
            router.openTrack(track.trackId)
        }
    }

    private fun clearSearch() {
        binding.inputSearchForm.setText("")
        viewModel.clearSearchText()
        if (adapterHistory.tracks.isNotEmpty()) {
            showContent(Content.TRACKS_HISTORY)
        } else {
            showContent(Content.SEARCH_RESULT)
        }
        val view = this.currentFocus
        if (view != null) {
            val input = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            input.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showTracks(tracks: List<Track>) {
        adapterSearch.updateTracks(tracks)
        showContent(Content.SEARCH_RESULT)
    }

    private fun showError() {
        showContent(Content.ERROR)
    }

    private fun showEmptyResult() {
        showContent(Content.NOT_FOUND)
    }

    private fun initHistory() {
        binding.recyclerViewHistory.adapter = adapterHistory

        if (binding.inputSearchForm.text.isEmpty()) {
            showContent(Content.SEARCH_RESULT)

            if (adapterHistory.tracks.isNotEmpty()) {
                showContent(Content.TRACKS_HISTORY)
            }
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
            showContent(Content.SEARCH_RESULT)
        }
    }

    private fun showHistory(tracks: List<Track>) {
        adapterHistory.updateTracks(tracks)
        viewModel.showHistoryTracks()
        showContent(Content.TRACKS_HISTORY)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyView()
    }

    private fun loadTracks() {
        viewModel.loadTracks(binding.inputSearchForm.text.toString())
    }

    private fun showLoading() {
        showContent(Content.LOADING)
    }

    private fun showContent(content: Content) {
        when (content) {
            Content.NOT_FOUND -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.VISIBLE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            Content.ERROR -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
            }
            Content.SEARCH_RESULT -> {
                binding.recyclerViewSearch.visibility = View.VISIBLE
                binding.historyList.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            Content.TRACKS_HISTORY -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.historyList.visibility = View.VISIBLE
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
            }
            Content.LOADING -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.searchProgressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH, textSearch)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(TEXT_SEARCH).toString()
        binding.inputSearchForm.setText(textSearch)
    }


    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }
}



