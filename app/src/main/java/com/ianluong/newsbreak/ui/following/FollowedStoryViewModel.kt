package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.*
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.StoryDatabase
import com.ianluong.newsbreak.database.StoryWithArticles

class FollowedStoryViewModel(private val app: Application) : AndroidViewModel(app) {
    // TODO: Implement the ViewModel
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