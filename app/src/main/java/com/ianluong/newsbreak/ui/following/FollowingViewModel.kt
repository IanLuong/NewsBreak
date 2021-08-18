package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.database.Story

class FollowingViewModel(app: Application) : AndroidViewModel(app) {

    private val storyRepository = StoryRepository.get()

    val storiesLiveData: LiveData<List<Story>> = storyRepository.getStories()

    fun updateStory() {
        storyRepository.updateStory(Story())
    }
}