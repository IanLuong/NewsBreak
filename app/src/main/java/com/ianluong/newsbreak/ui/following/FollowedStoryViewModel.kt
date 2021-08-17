package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.*
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.Story
import com.ianluong.newsbreak.database.StoryDatabase
import java.util.*

class FollowedStoryViewModel(): ViewModel() {
    private val storyRepository = StoryRepository.get()
    private val storyIDLiveData = MutableLiveData<UUID>()

    var articlesLiveData: LiveData<List<Article>> =
        Transformations.switchMap(storyIDLiveData) {
            storyRepository.getStoryWithArticles(storyIDLiveData.value!!)
        }

    fun loadArticles(story: Story) {
        storyIDLiveData.value = story.storyId
    }

    fun deleteStoryAndArticles(story: Story) {
        storyRepository.deleteStoryAndArticles(story)
    }
}