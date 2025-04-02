package com.example.hotelrecyclertest

import android.util.Log

class SearchHistoryHelper {
    private val history = mutableListOf<String>()
    private val maxHistorySize = 10

    fun addSearchQuery(query: String) {
        Log.d("SearchHistoryHelper", "Adding query: $query")
        history.remove(query) // Удаляем дубликаты
        history.add(0, query) // Добавляем в начало
        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1)
        }
        Log.d("SearchHistoryHelper", "Current history: $history")
    }

    fun getSearchHistory(): List<String> {
        Log.d("SearchHistoryHelper", "Retrieved history: $history")
        return history
    }

    fun clearHistory() {
        Log.d("SearchHistoryHelper", "Clearing history")
        history.clear()
    }
}