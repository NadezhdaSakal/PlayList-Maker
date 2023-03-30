package com.sakal.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.adapters.TrackRecyclerAdapter
import com.sakal.playlistmaker.model.ApiConstants
import com.sakal.playlistmaker.model.TrackResponse
import com.sakal.playlistmaker.model.iTunesSearchAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    lateinit var searchEditText: EditText
    lateinit var buttonClearSearch: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var historyList: LinearLayout
    private lateinit var buttonClearHistory: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var prefs: SharedPreferences

    private var textSearch = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(iTunesSearchAPI::class.java)

    private val searchAdapter = TrackRecyclerAdapter {
        searchHistory.add(it)
    }

    private val historyAdapter = TrackRecyclerAdapter {
        searchHistory.add(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initToolbar()

        initSearch()

        inputText()

        initSearchResults()

        initHistory()
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initSearch() {
        buttonRetry = findViewById(R.id.button_retry)
        buttonClearSearch = findViewById(R.id.button_clear_search_form)
        searchEditText = findViewById(R.id.input_search_form)
        searchEditText.setText(textSearch)
        searchEditText.requestFocus()

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack()
            }
            false
        }
        buttonRetry.setOnClickListener {
            getTrack()
        }

        buttonClearSearch.setOnClickListener {
            clearSearch()
        }
    }

    private fun initHistory() {
        buttonClearHistory = findViewById(R.id.button_clear_history)
        historyList = findViewById(R.id.history_list)
        recyclerHistory = findViewById(R.id.recyclerViewHistory)

        recyclerHistory.adapter = historyAdapter
        prefs = getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)

        if (searchEditText.text.isEmpty()) {
            historyAdapter.tracks = searchHistory.get()
            if (historyAdapter.tracks.isNotEmpty()) {
                showContent(Content.TRACKS_HISTORY)
            }
        }

        buttonClearHistory.setOnClickListener {
            clearTracksHistory()
        }
    }

    private fun clearTracksHistory() {
        searchHistory.clear()
        showContent(Content.SEARCH_RESULT)
    }

    private fun initSearchResults() {
        recyclerView = findViewById(R.id.recyclerViewSearch)
        recyclerView.adapter = searchAdapter
    }

    private fun clearSearch() {
        searchEditText.setText("")
        historyAdapter.tracks = searchHistory.get()
        if (historyAdapter.tracks.isNotEmpty()) {
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

    private fun searchClearIconVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun inputText() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonClearSearch.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.text.toString()

                if (searchEditText.hasFocus() && textSearch.isNotEmpty()) {
                    showContent(Content.SEARCH_RESULT)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchEditText.text.isEmpty()) {
                showContent(Content.SEARCH_RESULT)
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
        searchEditText.setText(textSearch)
    }

    private fun getTrack() {
        serviceSearch.searchTrack(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (textSearch.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code()
                        == ApiConstants.SUCCESS_CODE
                    ) {
                        searchAdapter.tracks.clear()
                        searchAdapter.tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        showContent(Content.SEARCH_RESULT)

                    } else {
                        showContent(Content.NOT_FOUND)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showContent(Content.ERROR)
                }
            })
    }

    private fun showContent(content: Content) {
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)

        when (content) {
            Content.NOT_FOUND -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.VISIBLE
                placeholderCommunicationsProblem.visibility = View.GONE

            }
            Content.ERROR -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.VISIBLE
            }
            Content.SEARCH_RESULT -> {
                recyclerView.visibility = View.VISIBLE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
            Content.TRACKS_HISTORY -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.VISIBLE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
        }
    }
}
