package com.sakal.playlistmaker


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.sakal.playlistmaker.adapters.SearchHistoryAdapter
import com.sakal.playlistmaker.adapters.TrackRecyclerAdapter
import com.sakal.playlistmaker.model.ApiConstants
import com.sakal.playlistmaker.model.Track
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
    lateinit var searchClearIcon: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var searchAdapter: TrackRecyclerAdapter
    lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var placeholderNothingWasFound: TextView
    private lateinit var placeholderCommunicationsProblem: LinearLayout
    private lateinit var buttonRetry: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerHistory: RecyclerView
    private lateinit var historyList: LinearLayout
    private lateinit var buttonClear: Button
    private lateinit var searchHistory: SearchHistory
    private lateinit var prefs: SharedPreferences

    var textSearch = ""

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(iTunesSearchAPI::class.java)
    private val tracks = ArrayList<Track>()
    private var historyTracks = ArrayList<Track>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initHistory()

        initRecycler(tracks)

        initSearchHistoryRecycler(historyTracks)

        visibleHistoryTrack()

        retry()

        initToolbar()

        initSearch()

        inputText()
    }

    private fun retry() {
        buttonRetry = findViewById(R.id.button_retry)
        buttonRetry.setOnClickListener {
            getTrack()
        }
    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initHistory() {
        historyList = findViewById(R.id.history_list)
        buttonClear = findViewById(R.id.button_clear_history)
        prefs = getSharedPreferences(Constants.HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)
        searchHistory = SearchHistory(prefs)
        historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>

        buttonClear.setOnClickListener {
            searchHistory.clearHistory()
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            searchHistoryAdapter.tracksHistory.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            historyList.visibility = View.GONE
        }
    }

    private fun initSearchHistoryRecycler(historyTracks: ArrayList<Track>) {
        recyclerHistory = findViewById(R.id.recyclerViewHistory)
        searchHistoryAdapter = SearchHistoryAdapter(historyTracks)
        recyclerHistory.adapter = searchHistoryAdapter
    }

    private fun visibleHistoryTrack() {
        if (historyTracks.isNotEmpty()) historyList.visibility = View.VISIBLE
        else historyList.visibility = View.GONE
    }


    private fun initRecycler(tracks: ArrayList<Track>) {
        recyclerView = findViewById(R.id.recycler_view)
        searchAdapter = TrackRecyclerAdapter(tracks)
        recyclerView.adapter = searchAdapter

        searchAdapter.itemClickListener = { position, track ->
            searchHistory.addTrack(track, position)
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            historyTracks.addAll(historyTracks)
        }
    }

    private fun initSearch() {
        searchClearIcon = findViewById(R.id.clear_form)
        searchEditText = findViewById(R.id.input_search_form)
        searchEditText.setText(textSearch)
        searchEditText.requestFocus()

        searchClearIcon.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            searchEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            placeholderNothingWasFound.isVisible = false
            placeholderNothingWasFound.isVisible = false
            tracks.clear()
            searchAdapter.notifyDataSetChanged()

            historyList.visibility = View.VISIBLE
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            searchHistoryAdapter.tracksHistory = historyTracks
            searchHistoryAdapter.notifyDataSetChanged()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack()
                true
            }
            false
        }
    }

    private fun inputText() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearIcon.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun searchClearIconVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
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
                    if (textSearch.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == ApiConstants.SUCCESS_CODE) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        showPlaceholder(PlaceHolder.SEARCH_RESULT)

                    } else {
                        showPlaceholder(PlaceHolder.NOT_FOUND)
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showPlaceholder(PlaceHolder.ERROR)
                }
            })
    }

    private fun showPlaceholder(placeholder: PlaceHolder) {

        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)

        when (placeholder) {
            PlaceHolder.NOT_FOUND -> {
                recyclerView.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.VISIBLE
            }
            PlaceHolder.ERROR -> {
                recyclerView.visibility = View.GONE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.VISIBLE
            }
            else -> {
                recyclerView.visibility = View.VISIBLE
                placeholderNothingWasFound.visibility = View.GONE
                placeholderCommunicationsProblem.visibility = View.GONE
            }
        }
    }

}
