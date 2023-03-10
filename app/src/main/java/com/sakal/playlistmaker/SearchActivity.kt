package com.sakal.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import com.sakal.playlistmaker.adapters.SearchRecyclerAdapter
import com.sakal.playlistmaker.databinding.ActivitySearchBinding
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
        const val SEARCH_QUERY = "SEARCH_QUERY"
        private val tracks = ArrayList<Track>()

    }

    private lateinit var tracksAdapter: SearchRecyclerAdapter
    private lateinit var binding: ActivitySearchBinding

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceSearch = retrofit.create(iTunesSearchAPI::class.java)

    private var searchText = ""
    private lateinit var searchInput: EditText
    private lateinit var searchInputClearButton: ImageView

    private val searchInputTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            searchInputClearButton.visibility = clearButtonVisibility(s)
            searchText = s.toString()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initToolbar()

        initSearch()

        initRecycler()
    }

    private fun initToolbar() {
        findViewById<Toolbar>(R.id.search_toolbar).setNavigationOnClickListener {
            finish()
        }
    }

    private fun initSearch() {

        searchInput = binding.inputSearchForm
        searchInput.requestFocus()
        searchInput.addTextChangedListener(searchInputTextWatcher)

        searchInputClearButton = binding.clearForm
        searchInputClearButton.visibility = clearButtonVisibility(searchInput.text)
        searchInputClearButton.setOnClickListener {
            clearSearchForm()
        }

    }

    private fun clearSearchForm() {
        searchInput.setText("")

        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_QUERY, "")
        searchInput.setText(searchText)
    }

    private fun searchTrack() {
        serviceSearch.searchTrack(searchInput.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (searchText.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == ApiConstants.SUCCESS_CODE) {
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        tracksAdapter.notifyDataSetChanged()
                        binding.placeholderNothingWasFound.isVisible = false
                        binding.placeholderCommunicationsProblem.isVisible = false
                    } else {
                        binding.placeholderNothingWasFound.isVisible = true
                        binding.placeholderCommunicationsProblem.isVisible = false
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    binding.placeholderCommunicationsProblem.isVisible = true
                    binding.placeholderNothingWasFound.isVisible = false
                }
            })
    }

    private fun initRecycler() {
        binding.recyclerView.apply {
            tracksAdapter = SearchRecyclerAdapter(tracks)
            //recyclerView.adapter = tracksAdapter

        }
    }

}
