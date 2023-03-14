package com.sakal.playlistmaker


import android.content.Context
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
import com.sakal.playlistmaker.adapters.SearchRecyclerAdapter
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
    var textSearch = ""
    lateinit var searchEditText: EditText
    lateinit var searchClearIcon: ImageView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var searchAdapter: SearchRecyclerAdapter
    lateinit var placeholderNothingWasFound: LinearLayout
    lateinit var placeholderCommunicationsProblem: LinearLayout
    lateinit var buttonReturn: Button

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(iTunesSearchAPI::class.java)

    private val tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        initRecycler(tracks)

        initViews()

        setListeners()

        initToolbar()

        inputText()
    }

    private fun initViews() {
        searchClearIcon = findViewById(R.id.clear_form)
        searchEditText = findViewById(R.id.input_search_form)
        searchEditText.setText(textSearch)
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonReturn = findViewById(R.id.button_return)
    }

    private fun setListeners() {

        searchClearIcon.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            searchEditText.setText("")
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            placeholderNothingWasFound.isVisible = false
            placeholderNothingWasFound.isVisible = false
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                getTrack()
                true
            }
            false
        }

        buttonReturn.setOnClickListener() {
            placeholderCommunicationsProblem.visibility = View.INVISIBLE
            getTrack()
        }

    }

    private fun initToolbar() {
        toolbar = findViewById(R.id.search_toolbar)
        toolbar.setNavigationOnClickListener() {
            finish()
        }
    }

    fun initRecycler(tracks: ArrayList<Track>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        searchAdapter = SearchRecyclerAdapter(tracks)
        recyclerView.adapter = searchAdapter
    }

    private fun inputText() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearIcon.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.getText().toString()
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

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    private fun getTrack() {
        serviceSearch.searchTrack(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (textSearch.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == ApiConstants.SUCCESS_CODE) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        placeholderNothingWasFound.isVisible = false
                        placeholderCommunicationsProblem.isVisible = false
                    } else {
                        placeholderNothingWasFound.isVisible = true
                        placeholderCommunicationsProblem.isVisible = false
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    placeholderCommunicationsProblem.isVisible = true
                    placeholderNothingWasFound.isVisible = false
                }
            })
    }
}
