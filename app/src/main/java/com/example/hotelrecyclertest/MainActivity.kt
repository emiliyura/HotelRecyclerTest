package com.example.hotelrecyclertest

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.Timer
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var searchList: ArrayList<DataClass>
    private lateinit var myAdapter: AdapterClass
    private lateinit var searchView: SearchView
    private lateinit var themeSwitch: Switch
    private lateinit var clearHistoryButton: Button
    private lateinit var searchProgress: ProgressBar
    private lateinit var searchHistoryHelper: SearchHistoryHelper
    private var searchTextTimer: Timer? = null
    private var isShowingHistory = false

    // Массивы данных (без инициализации строк)
    private val imageList = arrayOf(
        R.drawable.ic_list,
        R.drawable.ic_checkbox,
        R.drawable.ic_image,
        R.drawable.ic_toggle,
        R.drawable.ic_date,
        R.drawable.ic_rating,
        R.drawable.ic_time,
        R.drawable.ic_text,
        R.drawable.ic_edit,
        R.drawable.ic_camera
    )

    private val titleList = arrayOf(
        "ListView",
        "CheckBox",
        "ImageView",
        "Toggle Switch",
        "Date Picker",
        "Rating Bar",
        "Time Picker",
        "TextView",
        "EditText",
        "Camera"
    )

    // Будем инициализировать descList в onCreate()
    private lateinit var descList: Array<String>
    private val detailImageList = arrayOf(
        R.drawable.ic_date,
        R.drawable.ic_date,
        R.drawable.ic_edit,
        R.drawable.ic_list,
        R.drawable.ic_text,
        R.drawable.ic_time,
        R.drawable.ic_rating,
        R.drawable.ic_toggle,
        R.drawable.ic_toggle,
        R.drawable.ic_list
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация строковых ресурсов
        descList = arrayOf(
            getString(R.string.listview),
            getString(R.string.checkbox),
            getString(R.string.imageview),
            getString(R.string.toggle),
            getString(R.string.date),
            getString(R.string.rating),
            getString(R.string.time),
            getString(R.string.textview),
            getString(R.string.edit),
            getString(R.string.camera)
        )

        // Инициализация компонентов
        initViews()
        setupThemeSwitch()
        setupRecyclerView()
        setupSearchView()
        loadData()
    }

    // Остальной код остается без изменений
    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.search)
        themeSwitch = findViewById(R.id.themeSwitch)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        searchProgress = findViewById(R.id.searchProgress)
        searchHistoryHelper = SearchHistoryHelper(this)
    }

    private fun setupThemeSwitch() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        themeSwitch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES

        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            recreate()
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        dataList = ArrayList()
        searchList = ArrayList()
        myAdapter = AdapterClass(searchList)
        recyclerView.adapter = myAdapter

        myAdapter.onItemClick = {
            addToSearchHistory(it.dataTitle)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("android", it)
            startActivity(intent)
        }
    }

    private fun setupSearchView() {
        searchView.clearFocus()
        searchView.setOnSearchClickListener { showSearchHistory() }
        searchView.setOnCloseListener {
            isShowingHistory = false
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchTextTimer?.cancel()

                if (newText.isNullOrEmpty()) {
                    if (isShowingHistory) showSearchHistory()
                    else {
                        searchList.clear()
                        searchList.addAll(dataList)
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                    return false
                }

                searchProgress.visibility = View.VISIBLE
                searchTextTimer = Timer()
                searchTextTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        runOnUiThread { performSearch(newText) }
                    }
                }, 2000)

                return false
            }
        })
    }

    private fun performSearch(query: String) {
        searchList.clear()
        val searchText = query.toLowerCase(Locale.getDefault())
        dataList.forEach {
            if (it.dataTitle.toLowerCase(Locale.getDefault()).contains(searchText)) {
                searchList.add(it)
            }
        }
        recyclerView.adapter?.notifyDataSetChanged()
        isShowingHistory = false
        searchProgress.visibility = View.GONE
    }

    private fun showSearchHistory() {
        isShowingHistory = true
        val history = searchHistoryHelper.getSearchHistory()
        if (history.isNotEmpty()) {
            searchList.clear()
            history.forEach { query ->
                dataList.find { it.dataTitle.equals(query, ignoreCase = true) }?.let {
                    searchList.add(it)
                }
            }
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun addToSearchHistory(title: String) {
        searchHistoryHelper.addSearchQuery(title)
    }

    fun onClearHistoryClick(view: View) {
        searchHistoryHelper.clearHistory()
        if (isShowingHistory) {
            searchList.clear()
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun loadData() {
        for (i in imageList.indices) {
            val dataClass = DataClass(
                imageList[i],
                titleList[i],
                descList[i],
                detailImageList[i]
            )
            dataList.add(dataClass)
        }
        searchList.addAll(dataList)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}