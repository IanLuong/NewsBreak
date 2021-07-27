package com.ianluong.newsbreak.ui.newStories

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ianluong.newsbreak.ArticleRepository
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import java.util.*

class NewStoriesViewModel(private val app: Application) : AndroidViewModel(app) {

    val articlesLiveData: LiveData<List<Article>>

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    private val mutableSearchTerm = MutableLiveData<String>()

    private val newsFetcher = NewsFetcher()

    init {
        mutableSearchTerm.value = QueryPreferences.getQuery(app)

        articlesLiveData =
            Transformations.switchMap(mutableSearchTerm) {
                if (it.isBlank()) {
                    newsFetcher.searchUKHeadlines()
                } else {
                    newsFetcher.searchNews(it)
                }
            }

    }

    fun fetchSearch(query: String = "") {
        QueryPreferences.setQuery(query, app)
        mutableSearchTerm.value = query
    }

    fun fetchUKHeadlines() {
        QueryPreferences.setQuery("", app)
        mutableSearchTerm.value = ""
    }
}