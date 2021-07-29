package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.*
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.StoryDatabase
import com.ianluong.newsbreak.database.StoryWithArticles
import java.util.*

class FollowedStoryViewModel(): ViewModel() {
    // TODO: Implement the ViewModel
    private val storyRepository = StoryRepository.get()
    private val storyIDLiveData = MutableLiveData<UUID>()

    var articlesLiveData: LiveData<List<Article>> =
        Transformations.switchMap(storyIDLiveData) {
            storyRepository.getArticlesWithStoryID(storyIDLiveData.value!!)
        }

    fun loadArticles(storyID: UUID) {
        storyIDLiveData.value = storyID
    }
}