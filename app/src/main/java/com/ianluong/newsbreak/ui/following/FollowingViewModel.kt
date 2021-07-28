package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.database.Story
import java.util.*

class FollowingViewModel(app: Application) : AndroidViewModel(app) {

    private val storyRepository = StoryRepository.get()

    //TODO Take data from database //articleDatabase.getArticles()
    //val storiesLiveData: LiveData<List<Story>>
    private val mutableSearchTerm = MutableLiveData<String>()

    private val storyIDLiveData = MutableLiveData<UUID>()
    val storiesLiveData: LiveData<List<Story>> = Transformations.switchMap(mutableSearchTerm) {
        storyRepository.getStories()
    }

    /*
    init {
        mutableSearchTerm.value = QueryPreferences.getQuery(app)

        val responseLiveData: MutableLiveData<List<Story>> = MutableLiveData()
        responseLiveData.value = listOf(
            Story("Bob"),
            Story("Sam"),
            Story("Steve"),
            Story("AntiVax"),
            Story("Monument"),
        )

        storiesLiveData = Transformations.switchMap(mutableSearchTerm) {
            responseLiveData
        }
    }*/

    fun updateStory() {
        storyRepository.insertStory(Story("Sam"))
    }

    fun addArticle() {

    }
}