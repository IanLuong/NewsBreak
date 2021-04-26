package com.ianluong.newsbreak.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ianluong.newsbreak.ArticleRepository
import com.ianluong.newsbreak.NewsFetcher

class FollowingViewModel : ViewModel() {

    private val articleDatabase = ArticleRepository.get()

    //TODO Take data from database
    val articles = NewsFetcher().fetchNews("BBCHeadlines")//articleDatabase.getArticles()

}