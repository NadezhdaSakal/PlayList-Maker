package com.sakal.playlistmaker.search.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sakal.playlistmaker.Constants
import com.sakal.playlistmaker.databinding.ActivitySearchBinding
import com.sakal.playlistmaker.player.ui.activity.AudioPlayerActivity
import com.sakal.playlistmaker.search.domain.Track
import com.sakal.playlistmaker.search.ui.Router
import com.sakal.playlistmaker.search.ui.adapters.TrackAdapter
import com.sakal.playlistmaker.search.ui.view_model.SearchViewModel


class SearchActivity : ComponentActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter
    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSearchResults()

        initHistory()

        initEditText(savedInstanceState)

        handleButtons()

        router = Router(this)


        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        binding.searchToolbar.setNavigationOnClickListener {
            router.goBack()

        }

        viewModel.screenState.observe(this) { screenState ->
            if (viewModel.isReadyToRender(screenState, binding.inputSearchForm.text.toString())) {
                searchAdapter.setTracks(screenState.tracks)
                screenState.render(binding)
            }
        }

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
        outState.putString(INPUT_TEXT, binding.inputSearchForm.text.toString())
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
        searchAdapter = TrackAdapter {
            if (viewModel.trackOnClickDebounce()) {
                viewModel.addToHistory(it)
                navigateTo(AudioPlayerActivity::class.java, it)
            }
        }

        binding.recyclerViewSearch.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

    }

    private  fun initHistory() {
        historyAdapter = TrackAdapter {
            navigateTo(AudioPlayerActivity::class.java, it)
        }

        binding.recyclerViewHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(this.context)
        }

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
                        viewModel.showHistory()
                    }
                    viewModel.searchDebounce(binding.inputSearchForm.text.toString())
                    binding.buttonClearSearchForm.visibility =
                        clearButtonVisibility(text.toString())
                }
            }
    }

    private fun clearButtonVisibility(p0: CharSequence?) =
        if (p0.isNullOrEmpty()) View.GONE else View.VISIBLE

    private fun restoreTextFromBundle(textField: EditText, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getString(INPUT_TEXT)!!.isNotEmpty()) {
                textField.setText(savedInstanceState.getString(INPUT_TEXT)!!)
                viewModel.getTracks(savedInstanceState.getString(INPUT_TEXT)!!)
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

    private fun navigateTo(clazz: Class<out ComponentActivity>, track: Track) {
        val intent = Intent(this, clazz)
        intent.putExtra(Constants.TRACK, track)
        startActivity(intent)
    }


    companion object {
        private const val INPUT_TEXT = "INPUT_TEXT"
    }
}
