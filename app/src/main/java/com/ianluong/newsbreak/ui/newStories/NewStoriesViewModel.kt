package com.ianluong.newsbreak.ui.newStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.api.Article
import java.util.*

class NewStoriesViewModel : ViewModel() {

    val articlesLiveData: LiveData<List<Article>> = NewsFetcher().fetchBBCHeadlines()

}