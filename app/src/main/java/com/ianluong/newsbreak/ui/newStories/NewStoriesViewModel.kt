package com.ianluong.newsbreak.ui.newStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ianluong.newsbreak.ArticleRepository
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.api.Article
import java.util.*

class NewStoriesViewModel : ViewModel() {

    val articlesLiveData: LiveData<List<Article>>

    private val articleRepository: ArticleRepository = ArticleRepository.get()
    private val mutableSearchTerm = MutableLiveData<String>()

    private val newsFetcher = NewsFetcher()

    init {
        mutableSearchTerm.value = "UKHeadlines"

        articlesLiveData =
            Transformations.switchMap(mutableSearchTerm) {
                newsFetcher.fetchNews(it) //UKHeadlines is the default on startup
            }

    }

    fun fetchNews(query: String = "") {
        mutableSearchTerm.value = query
    }

}