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
    //Переменная для работы с вводимым запросом
    var textSearch = ""
    //Переменные для работы с UI
    lateinit var searchEditText: EditText
    lateinit var searchClearIcon: ImageView
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var searchAdapter: SearchRecyclerAdapter
    lateinit var placeholderNothingWasFound: LinearLayout
    lateinit var placeholderCommunicationsProblem: LinearLayout
    lateinit var buttonReturn: Button

    //Подключаем Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceiTunesSearch = retrofit.create(iTunesSearchAPI::class.java)

    private val tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //RecyclerView
        setAdapter(tracks)

        //Присвоить значение переменным
        initViews()

        //Listener
        setListeners()

        //Работа с вводимым текстом
        inputText()
    }

    //Присвоить значение переменным
    private fun initViews(){
        searchClearIcon = findViewById(R.id.clear_form)
        searchEditText = findViewById(R.id.input_search_form)
        searchEditText.setText(textSearch)
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonReturn = findViewById(R.id.button_return)

        buttonArrowBackSettings = findViewById(R.id.search_toolbar)
    }

    //Настроить Listeners
    private fun setListeners(){
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener(){
            finish()
        }

        searchClearIcon.setOnClickListener {
            //Объект для работы с клавиатурой
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            //Очистить поле для ввода
            searchEditText.setText("")
            //Скрыть клавиатуру
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            //Убрать информацию о неудачных запросах
            placeholderNothingWasFound.isVisible  = false
            placeholderNothingWasFound.isVisible = false
            //Очистить найденный список треков
            tracks.clear()
            searchAdapter.notifyDataSetChanged()
        }

        //Поиск трека
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        //Повторить предыдущий запрос после нажатия на кнопку "Обновить"
        buttonReturn.setOnClickListener(){
            placeholderCommunicationsProblem.visibility = View.INVISIBLE
            searchTrack()
        }

    }

    //Настроить RecyclerView
    fun setAdapter(tracks : ArrayList<Track>){
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        searchAdapter = SearchRecyclerAdapter(tracks)
        recyclerView.adapter = searchAdapter
    }

    //Работа с вводимым текстом
    private fun inputText(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            //Действие при вводе текста
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearIcon.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.getText().toString()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        //Связать поля для ввода и TextWatcher
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    //Скрыть или показать кнопку для сброса ввода
    private fun searchClearIconVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //Cохранить уже имеющееся состояние Activity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH,textSearch)
    }

    //Получить сохранённое значение Activity из onSaveInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(TEXT_SEARCH).toString()
        searchEditText.setText(textSearch)
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    //Поиск трека черезе Retrofit
    private fun searchTrack(){
        serviceiTunesSearch.searchTrack(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (textSearch.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == 200){
                        tracks.clear()
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()
                        placeholderNothingWasFound.isVisible = false
                        placeholderCommunicationsProblem.isVisible = false
                    }
                    else{
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
