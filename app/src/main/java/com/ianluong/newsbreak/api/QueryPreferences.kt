package com.ianluong.newsbreak.api

import android.content.Context
import androidx.preference.PreferenceManager

private const val PREF_SEARCH_QUERY = "searchQuery"

object QueryPreferences {

    fun getQuery(context: Context): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(PREF_SEARCH_QUERY, "")!!
    }

    fun setQuery(query: String, context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context)
            .edit().putString(PREF_SEARCH_QUERY, query).apply()
    }
}