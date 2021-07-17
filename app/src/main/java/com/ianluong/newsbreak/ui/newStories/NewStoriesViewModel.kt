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
    private val articleIdLiveData = MutableLiveData<UUID>()



    var articleLiveData: LiveData<Article?> =
        Transformations.switchMap(articleIdLiveData) {
            articleRepository.getArticle(it)
        }

    init {
        articlesLiveData = NewsFetcher().fetchNews("UKHeadlines") //UKHeadlines is the default on startup
    }

    fun loadArticle(articleId: UUID) {
        articleIdLiveData.value = articleId
    }

    fun saveArticle(article: Article){
        articleRepository.insertArticle(article)
    }
}