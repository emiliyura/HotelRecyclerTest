package com.example.hotelrecyclertest

import android.content.Context

class SearchHistoryHelper(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
    private val maxHistorySize = 10

    fun addSearchQuery(query: String) {
        val history = getSearchHistory().toMutableList()
        history.remove(query) // Удаляем дубликаты
        history.add(0, query) // Добавляем в начало
        if (history.size > maxHistorySize) {
            history.removeAt(history.size - 1) // Ограничиваем размер
        }
        sharedPreferences.edit().putStringSet("history", history.toSet()).apply()
    }

    fun getSearchHistory(): List<String> {
        return sharedPreferences.getStringSet("history", setOf())?.toList() ?: listOf()
    }

    fun clearHistory() {
        sharedPreferences.edit().remove("history").apply()
    }
}