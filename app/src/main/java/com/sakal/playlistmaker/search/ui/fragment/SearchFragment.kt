package com.sakal.playlistmaker.search.ui.fragment

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sakal.playlistmaker.ApiConstants
import com.sakal.playlistmaker.R
import com.sakal.playlistmaker.databinding.FragmentSearchBinding
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.Content
import com.sakal.playlistmaker.search.ui.SearchScreenState
import com.sakal.playlistmaker.search.ui.adapters.TrackAdapter
import com.sakal.playlistmaker.search.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter = TrackAdapter {
        clickOnTrack(it)
    }

    private val historyAdapter = TrackAdapter {
        clickOnTrack(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            observeState().observe(viewLifecycleOwner) {
                render(it)
            }
        }

        initInput()

        initSearchResults()

        initHistory()
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
                when (state.error) {
                    ApiConstants.NO_INTERNET_CONNECTION_CODE ->
                        binding.errorText.text =
                            resources.getText(R.string.check_internet_connection)

                    else -> binding.errorText.text = String.format(
                        resources.getText(R.string.error).toString(),
                        state.error
                    )
                }
                showContent(Content.ERROR)
            }

            is SearchScreenState.NothingFound -> showContent(Content.NOT_FOUND)
            is SearchScreenState.Loading -> showContent(Content.LOADING)
        }
    }

    private fun initInput() {

        binding.inputSearchForm.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.buttonClearSearchForm.visibility = clearButtonVisibility(s)
            if (
                binding.inputSearchForm.hasFocus()
                && s.toString().isNotEmpty()
                && binding.historyList.visibility == View.VISIBLE
            ) {
                showContent(Content.SEARCH_RESULT)
            }
            viewModel.searchDebounce(binding.inputSearchForm.text.toString())
        }

        binding.inputSearchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.getTracks(binding.inputSearchForm.text.toString())
            }
            false
        }

        binding.buttonRetry.setOnClickListener {
            viewModel.getTracks(binding.inputSearchForm.text.toString())
        }

        binding.buttonClearSearchForm.visibility =
            clearButtonVisibility(binding.inputSearchForm.text)

        binding.inputSearchForm.requestFocus()

        binding.buttonClearSearchForm.setOnClickListener {
            clearSearch()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearch() {
        searchAdapter.tracks = arrayListOf()
        binding.inputSearchForm.setText("")
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        viewModel.clearSearch()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT_TEXT, binding.inputSearchForm.toString())
    }

    private fun initSearchResults() {
        binding.recyclerViewSearch.adapter = searchAdapter
    }

    private fun initHistory() {
        binding.recyclerViewHistory.adapter = historyAdapter
        binding.buttonClearHistory.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun clickOnTrack(track: Track) {
        if (!viewModel.isClickable) return
        viewModel.addToHistory(track)
        viewModel.onTrackClick()
        findNavController().navigate(R.id.action_searchFragment_to_audioPlayerActivity)
    }

    @SuppressLint("NotifyDataSetChanged")
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