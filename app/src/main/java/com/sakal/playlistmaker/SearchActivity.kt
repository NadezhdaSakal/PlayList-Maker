package com.sakal.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.sakal.playlistmaker.adapters.TrackRecyclerAdapter
import com.sakal.playlistmaker.model.ApiConstants
import com.sakal.playlistmaker.model.Track
import com.sakal.playlistmaker.model.TrackResponse
import com.sakal.playlistmaker.model.iTunesSearchAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() {

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
    private lateinit var progressBar: ProgressBar

    private var textSearch = ""

    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(Constants.CALL_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .readTimeout(Constants.READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val serviceSearch = retrofit.create(iTunesSearchAPI::class.java)

    private val searchAdapter = TrackRecyclerAdapter {
        clickOnTrack(it)
    }

    private val historyAdapter = TrackRecyclerAdapter {
        clickOnTrack(it)
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { getTrack() }


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
                true
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

    private fun clickOnTrack(track: Track) {
        if (clickDebounce()) {
            searchHistory.add(track)
            val intent = Intent(this, AudioPlayerActivity::class.java).apply {
                putExtra(Constants.TRACK, Gson().toJson(track))
            }
            startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
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
            historyAdapter.notifyDataSetChanged()

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

    private fun buttonSearchClearVisibility(s: CharSequence?): Int {
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

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                buttonClearSearch.visibility = buttonSearchClearVisibility(s)
                showContent(Content.LOADING)
                textSearch = searchEditText.text.toString()

                if (searchEditText.hasFocus() && textSearch.isNotEmpty()) {
                    searchDebounce()
                    historyAdapter.notifyDataSetChanged()
                } else if (searchEditText.hasFocus() && s?.isNotEmpty() == false && searchHistory.get()
                        .isNotEmpty()
                ) {
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
        searchEditText.addTextChangedListener(simpleTextWatcher)

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
        if (textSearch.isNotEmpty()) {
            serviceSearch.searchTrack(textSearch).enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    when (response.code()) {
                        ApiConstants.SUCCESS_CODE -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchAdapter.tracks = response.body()?.results!!
                                showContent(Content.SEARCH_RESULT)
                            } else {
                                showContent(Content.NOT_FOUND)
                            }
                        }
                        else -> {
                            showContent(Content.ERROR)
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showContent(Content.ERROR)
                }
            })
        }
    }

    private fun showContent(content: Content) {
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        progressBar = findViewById(R.id.searchProgressBar)


        when (content) {
            Content.NOT_FOUND -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.VISIBLE
                placeholderCommunicationsProblem.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            Content.ERROR -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
            Content.SEARCH_RESULT -> {
                recyclerView.visibility = View.VISIBLE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            Content.TRACKS_HISTORY -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.VISIBLE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            Content.LOADING -> {
                recyclerView.visibility = View.GONE
                historyList.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, Constants.SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, Constants.CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }
}
