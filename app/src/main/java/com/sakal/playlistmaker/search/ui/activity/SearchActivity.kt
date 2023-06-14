package com.sakal.playlistmaker.search.ui.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.sakal.playlistmaker.databinding.ActivitySearchBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.Content
import com.sakal.playlistmaker.search.ui.Router
import com.sakal.playlistmaker.search.ui.SearchScreenState
import com.sakal.playlistmaker.search.ui.adapters.TrackAdapter
import com.sakal.playlistmaker.search.ui.view_model.SearchViewModel


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var router: Router

    private val searchAdapter = TrackAdapter {
        clickOnTrack(it)
    }

    private val historyAdapter = TrackAdapter {
        clickOnTrack(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()

        initEditText(savedInstanceState)

        initSearchResults()

        initHistory()

        handleButtons()

        router = Router(this)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        viewModel.apply {

            observeState().observe(this@SearchActivity) {
                render(it)
            }

            observeShowToast().observe(this@SearchActivity) {
                showToast(it)
            }

        }
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Success -> {
                searchAdapter.tracks = state.tracks
                showContent(Content.SEARCH_RESULT)
            }
            is SearchScreenState.ShowHistory -> {
                historyAdapter.tracks = state.tracks
                showContent(Content.TRACKS_HISTORY)
            }
            is SearchScreenState.Error -> {
                binding.errorText.text = state.message
                showContent(Content.ERROR)
            }
            is SearchScreenState.NothingFound -> showContent(Content.NOT_FOUND)
            is SearchScreenState.Loading -> showContent(Content.LOADING)

        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun initToolbar() {
        binding.searchToolbar.setNavigationOnClickListener {
            router.goBack()
        }
    }

    private fun clickOnTrack(track: Track) {
        if (viewModel.trackIsClickable.value == false) return
        viewModel.onSearchClicked(track)
        router.openAudioPlayer(track)
    }

    override fun onResume() {
        super.onResume()
        if (binding.inputSearchForm.text.toString().isNotEmpty()) {
            viewModel.getTracks(binding.inputSearchForm.text.toString())
        } else {
            viewModel.showHistory()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.inputSearchForm.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun handleButtons() {
        binding.buttonClearSearchForm.apply {
            setOnClickListener {
                viewModel.showHistory()
                binding.inputSearchForm.text.clear()
                binding.inputSearchForm.onEditorAction(EditorInfo.IME_ACTION_DONE)
            }
        }

        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }

        binding.buttonRetry.apply {
            setOnClickListener {
                if (binding.inputSearchForm.text.toString().isNotEmpty()) {
                    viewModel.getTracks()
                }
            }
        }
    }

    private fun initSearchResults() {
        binding.recyclerViewSearch.adapter = searchAdapter
    }

    private fun initHistory() {
        binding.recyclerViewHistory.adapter = historyAdapter
    }

    private fun initEditText(savedInstanceState: Bundle?) {
        binding.inputSearchForm
            .apply {
                restoreTextFromBundle(textField = this, savedInstanceState = savedInstanceState)
                setOnEditorActionListener { _, actionId, _ ->
                    onClickOnEnterOnVirtualKeyboard(actionId)
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus && binding.inputSearchForm.text.isEmpty()) viewModel.showHistory()
                }

                doOnTextChanged { text, _, _, _ ->
                    if (binding.inputSearchForm.hasFocus() && text.toString().isEmpty()) {
                        showContent(Content.SEARCH_RESULT)
                    }
                    viewModel.searchDebounce(binding.inputSearchForm.text.toString())

                    binding.buttonClearSearchForm.visibility = clearButtonVisibility(text.toString())
                }
            }
    }

    private fun clearButtonVisibility(p0: CharSequence?) = if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE


    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val savedText: String? = savedInstanceState.getString(INPUT_TEXT)
            savedText?.let {
                textField.setText(it)
                viewModel.getTracks(it)
            }
        }
    }

    private fun onClickOnEnterOnVirtualKeyboard(actionId: Int): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (binding.inputSearchForm.text.toString().isNotEmpty()) {
                viewModel.getTracks(binding.inputSearchForm.text.toString())
            }
        }
        return false
    }

    private fun showContent(content: Content) {
        when (content) {
            Content.NOT_FOUND -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.VISIBLE
            }

            Content.ERROR -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.VISIBLE
                binding.historyList.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
            }

            Content.TRACKS_HISTORY -> {
                historyAdapter.notifyDataSetChanged()
                binding.recyclerViewSearch.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.historyList.visibility = View.VISIBLE
                binding.searchProgressBar.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
            }

            Content.SEARCH_RESULT -> {
                searchAdapter.notifyDataSetChanged()
                binding.recyclerViewSearch.visibility = View.VISIBLE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.searchProgressBar.visibility = View.GONE
                binding.placeholderNothingWasFound.visibility = View.GONE
            }

            Content.LOADING -> {
                binding.recyclerViewSearch.visibility = View.GONE
                binding.placeholderCommunicationsProblem.visibility = View.GONE
                binding.historyList.visibility = View.GONE
                binding.searchProgressBar.visibility = View.VISIBLE
                binding.placeholderNothingWasFound.visibility = View.GONE
            }
        }
    }

    companion object {
        private const val INPUT_TEXT = "INPUT_TEXT"
    }
}
