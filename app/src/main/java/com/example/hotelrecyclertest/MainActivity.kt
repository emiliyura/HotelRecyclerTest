//package com.example.hotelrecyclertest
//
//import android.content.Intent
//import android.content.res.Configuration
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.ProgressBar
//import android.widget.Switch
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.app.AppCompatDelegate
//import androidx.appcompat.widget.SearchView
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import java.util.*
//import kotlin.collections.ArrayList
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var searchHistoryRecyclerView: RecyclerView
//    private lateinit var dataList: ArrayList<DataClass>
//    private lateinit var searchList: ArrayList<DataClass>
//    private lateinit var searchHistoryList: ArrayList<String>
//    private lateinit var myAdapter: AdapterClass
//    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
//    private lateinit var searchView: SearchView
//    private lateinit var themeSwitch: Switch
//    private lateinit var clearHistoryButton: Button
//    private lateinit var searchProgress: ProgressBar
//    private lateinit var searchHistoryHelper: SearchHistoryHelper
//
//    private val imageList = arrayOf(
//        R.drawable.burjalarab, R.drawable.ritzcarlton, R.drawable.marinabaysands,
//        R.drawable.hoteldeparis, R.drawable.theplaza, R.drawable.frontenac,
//        R.drawable.emiratespalace, R.drawable.peninsula, R.drawable.lakepalace,
//        R.drawable.fourseasons
//    )
//
////    private val titleList = arrayOf(
////        "ListView", "CheckBox", "ImageView", "Toggle Switch", "Date Picker",
////        "Rating Bar", "Time Picker", "TextView", "EditText", "Camera"
////    )
//    // отели
//    private val titleList = arrayOf(
//        "Burj Al Arab",
//        "The Ritz-Carlton",
//        "Marina Bay Sands",
//        "Hotel de Paris",
//        "The Plaza",
//        "Fairmont Le Château Frontenac",
//        "Emirates Palace",
//        "The Peninsula",
//        "Taj Lake Palace",
//        "Four Seasons"
//    )
//
//    private lateinit var descList: Array<String>
//    private val detailImageList = arrayOf(
//        R.drawable.burjalarab, R.drawable.ritzcarlton, R.drawable.marinabaysands,
//        R.drawable.hoteldeparis, R.drawable.theplaza, R.drawable.frontenac,
//        R.drawable.emiratespalace, R.drawable.peninsula, R.drawable.lakepalace,
//        R.drawable.fourseasons
//    )
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
////        descList = arrayOf(
////            getString(R.string.listview), getString(R.string.checkbox), getString(R.string.imageview),
////            getString(R.string.toggle), getString(R.string.date), getString(R.string.rating),
////            getString(R.string.time), getString(R.string.textview), getString(R.string.edit),
////            getString(R.string.camera)
////        )
//        descList = arrayOf(
//            getString(R.string.burj_al_arab),
//            getString(R.string.the_ritz_carlton),
//            getString(R.string.marina_bay_sands),
//            getString(R.string.hotel_de_paris),
//            getString(R.string.the_plaza),
//            getString(R.string.fairmont_le_chateau_frontenac),
//            getString(R.string.emirates_palace),
//            getString(R.string.the_peninsula),
//            getString(R.string.taj_lake_palace),
//            getString(R.string.four_seasons)
//        )
//
//        initViews()
//        setupThemeSwitch()
//        setupRecyclerViews()
//        setupSearchView()
//        loadData()
//    }
//
//    private fun initViews() {
//        recyclerView = findViewById(R.id.recyclerView)
//        searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView)
//        searchView = findViewById(R.id.search)
//        themeSwitch = findViewById(R.id.themeSwitch)
//        clearHistoryButton = findViewById(R.id.clearHistoryButton)
//        searchProgress = findViewById(R.id.searchProgress)
//        searchHistoryHelper = SearchHistoryHelper() // Без контекста для теста
//        searchHistoryList = ArrayList()
//    }
//
//    private fun setupThemeSwitch() {
//        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        themeSwitch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
//        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
//            AppCompatDelegate.setDefaultNightMode(
//                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
//                else AppCompatDelegate.MODE_NIGHT_NO
//            )
//            recreate()
//        }
//    }
//
//    private fun setupRecyclerViews() {
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.setHasFixedSize(true)
//        dataList = ArrayList()
//        searchList = ArrayList()
//        myAdapter = AdapterClass(searchList)
//        recyclerView.adapter = myAdapter
//
//        myAdapter.onItemClick = {
//            addToSearchHistory(it.dataTitle)
//            val intent = Intent(this, DetailActivity::class.java)
//            intent.putExtra("android", it)
//            startActivity(intent)
//        }
//
//        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
//        searchHistoryAdapter = SearchHistoryAdapter(searchHistoryList) { query ->
//            searchView.setQuery(query, true)
//            searchHistoryRecyclerView.visibility = View.GONE
//        }
//        searchHistoryRecyclerView.adapter = searchHistoryAdapter
//        searchHistoryRecyclerView.visibility = View.GONE
//    }
//
//    private fun setupSearchView() {
//        searchView.clearFocus()
//        searchView.setOnSearchClickListener {
//            showSearchHistory()
//        }
//        searchView.setOnCloseListener {
//            searchHistoryRecyclerView.visibility = View.GONE
//            false
//        }
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let {
//                    addToSearchHistory(it)
//                    performSearch(it)
//                }
//                searchView.clearFocus()
//                searchHistoryRecyclerView.visibility = View.GONE
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText.isNullOrEmpty()) {
//                    showSearchHistory()
//                } else {
//                    performSearch(newText)
//                }
//                return false
//            }
//        })
//    }
//
//    private fun performSearch(query: String) {
//        searchList.clear()
//        val searchText = query.lowercase(Locale.getDefault())
//        dataList.forEach {
//            if (it.dataTitle.lowercase(Locale.getDefault()).contains(searchText)) {
//                searchList.add(it)
//            }
//        }
//        recyclerView.adapter?.notifyDataSetChanged()
//        searchHistoryRecyclerView.visibility = View.GONE
//        searchProgress.visibility = View.GONE
//    }
//
//    private fun showSearchHistory() {
//        Log.d("SearchHistory", "Showing history")
//        searchHistoryList.clear()
//        val history = searchHistoryHelper.getSearchHistory()
//        if (history.isNotEmpty()) {
//            searchHistoryList.addAll(history)
//            searchHistoryAdapter.notifyDataSetChanged()
//            searchHistoryRecyclerView.visibility = View.VISIBLE
//            Log.d("SearchHistory", "History shown: $searchHistoryList")
//        } else {
//            searchHistoryRecyclerView.visibility = View.GONE
//            Log.d("SearchHistory", "No history available")
//        }
//    }
//
//    private fun addToSearchHistory(query: String) {
//        Log.d("SearchHistory", "Adding query: $query")
//        searchHistoryHelper.addSearchQuery(query)
//    }
//
//    fun onClearHistoryClick(view: View) {
//        searchHistoryHelper.clearHistory()
//        searchHistoryList.clear()
//        searchHistoryAdapter.notifyDataSetChanged()
//        searchHistoryRecyclerView.visibility = View.GONE
//        Log.d("SearchHistory", "History cleared")
//    }
//
//    private fun loadData() {
//        for (i in imageList.indices) {
//            val dataClass = DataClass(imageList[i], titleList[i], descList[i], detailImageList[i])
//            dataList.add(dataClass)
//        }
//        searchList.addAll(dataList)
//        recyclerView.adapter?.notifyDataSetChanged()
//    }
//}
//
//class SearchHistoryAdapter(
//    private val historyList: ArrayList<String>,
//    private val onItemClick: (String) -> Unit
//) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {
//
//    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val historyItem: TextView = itemView.findViewById(R.id.history_item)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val query = historyList[position]
//        holder.historyItem.text = query
//        holder.itemView.setOnClickListener {
//            Log.d("SearchHistoryAdapter", "Clicked: $query")
//            onItemClick(query)
//        }
//    }
//
//    override fun getItemCount(): Int = historyList.size
//}



package com.example.hotelrecyclertest

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchHistoryRecyclerView: RecyclerView
    private lateinit var dataList: ArrayList<DataClass>
    private lateinit var searchList: ArrayList<DataClass>
    private lateinit var searchHistoryList: ArrayList<String>
    private lateinit var myAdapter: AdapterClass
    private lateinit var searchHistoryAdapter: SearchHistoryAdapter
    private lateinit var searchView: SearchView
    private lateinit var themeSwitch: Switch
    private lateinit var clearHistoryButton: Button
    private lateinit var searchProgress: ProgressBar
    private lateinit var searchHistoryHelper: SearchHistoryHelper

    private val imageList = arrayOf(
        R.drawable.burjalarab, R.drawable.ritzcarlton, R.drawable.marinabaysands,
        R.drawable.hoteldeparis, R.drawable.theplaza, R.drawable.frontenac,
        R.drawable.emiratespalace, R.drawable.peninsula, R.drawable.lakepalace,
        R.drawable.fourseasons
    )

    private val titleList = arrayOf(
        "Burj Al Arab",
        "The Ritz-Carlton",
        "Marina Bay Sands",
        "Hotel de Paris",
        "The Plaza",
        "Fairmont Le Château Frontenac",
        "Emirates Palace",
        "The Peninsula",
        "Taj Lake Palace",
        "Four Seasons"
    )

    private lateinit var descList: Array<String>
    private val detailImageList = arrayOf(
        R.drawable.burjalarab, R.drawable.ritzcarlton, R.drawable.marinabaysands,
        R.drawable.hoteldeparis, R.drawable.theplaza, R.drawable.frontenac,
        R.drawable.emiratespalace, R.drawable.peninsula, R.drawable.lakepalace,
        R.drawable.fourseasons
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        descList = arrayOf(
            getString(R.string.burj_al_arab),
            getString(R.string.the_ritz_carlton),
            getString(R.string.marina_bay_sands),
            getString(R.string.hotel_de_paris),
            getString(R.string.the_plaza),
            getString(R.string.fairmont_le_chateau_frontenac),
            getString(R.string.emirates_palace),
            getString(R.string.the_peninsula),
            getString(R.string.taj_lake_palace),
            getString(R.string.four_seasons)
        )

        initViews()
        setupThemeSwitch()
        setupRecyclerViews()
        setupSearchView()
        loadData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        searchHistoryRecyclerView = findViewById(R.id.searchHistoryRecyclerView)
        searchView = findViewById(R.id.search)
        themeSwitch = findViewById(R.id.themeSwitch)
        clearHistoryButton = findViewById(R.id.clearHistoryButton)
        searchProgress = findViewById(R.id.searchProgress)
        searchHistoryHelper = SearchHistoryHelper()
        searchHistoryList = ArrayList()
    }

    private fun setupThemeSwitch() {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        themeSwitch.isChecked = currentNightMode == Configuration.UI_MODE_NIGHT_YES
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate()
        }
    }

    private fun setupRecyclerViews() {
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

        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryAdapter = SearchHistoryAdapter(searchHistoryList) { query ->
            searchView.setQuery(query, true)
            searchHistoryRecyclerView.visibility = View.GONE
        }
        searchHistoryRecyclerView.adapter = searchHistoryAdapter
        searchHistoryRecyclerView.visibility = View.GONE
    }

    private fun setupSearchView() {
        searchView.clearFocus()
        searchView.setOnSearchClickListener {
            showSearchHistory()
        }
        searchView.setOnCloseListener {
            searchHistoryRecyclerView.visibility = View.GONE
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    addToSearchHistory(it)
                    performSearch(it)
                }
                searchView.clearFocus()
                searchHistoryRecyclerView.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    searchProgress.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    showSearchHistory()
                } else {
                    searchProgress.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    performSearch(newText)
                }
                return false
            }
        })
    }

    private fun performSearch(query: String) {
        searchProgress.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        // Имитация задержки поиска (в реальном приложении здесь может быть сетевой запрос)
        recyclerView.postDelayed({
            searchList.clear()
            val searchText = query.lowercase(Locale.getDefault())
            dataList.forEach {
                if (it.dataTitle.lowercase(Locale.getDefault()).contains(searchText)) {
                    searchList.add(it)
                }
            }
            recyclerView.adapter?.notifyDataSetChanged()
            searchProgress.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            searchHistoryRecyclerView.visibility = View.GONE
        }, 500)
    }

    private fun showSearchHistory() {
        Log.d("SearchHistory", "Showing history")
        searchHistoryList.clear()
        val history = searchHistoryHelper.getSearchHistory()
        if (history.isNotEmpty()) {
            searchHistoryList.addAll(history)
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryRecyclerView.visibility = View.VISIBLE
            Log.d("SearchHistory", "History shown: $searchHistoryList")
        } else {
            searchHistoryRecyclerView.visibility = View.GONE
            Log.d("SearchHistory", "No history available")
        }
    }

    private fun addToSearchHistory(query: String) {
        Log.d("SearchHistory", "Adding query: $query")
        searchHistoryHelper.addSearchQuery(query)
    }

    fun onClearHistoryClick(view: View) {
        searchHistoryHelper.clearHistory()
        searchHistoryList.clear()
        searchHistoryAdapter.notifyDataSetChanged()
        searchHistoryRecyclerView.visibility = View.GONE
        Log.d("SearchHistory", "History cleared")
    }

    private fun loadData() {
        for (i in imageList.indices) {
            val dataClass = DataClass(imageList[i], titleList[i], descList[i], detailImageList[i])
            dataList.add(dataClass)
        }
        searchList.addAll(dataList)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}

class SearchHistoryAdapter(
    private val historyList: ArrayList<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val historyItem: TextView = itemView.findViewById(R.id.history_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val query = historyList[position]
        holder.historyItem.text = query
        holder.itemView.setOnClickListener {
            Log.d("SearchHistoryAdapter", "Clicked: $query")
            onItemClick(query)
        }
    }

    override fun getItemCount(): Int = historyList.size
}