package com.ianluong.newsbreak.ui.following

import android.app.Application
import androidx.lifecycle.*
import com.ianluong.newsbreak.ArticleRepository
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.Story
import kotlin.random.Random
import kotlin.random.asJavaRandom

class FollowingViewModel(app: Application) : AndroidViewModel(app) {

    //private val articleDatabase = ArticleRepository.get()

    //TODO Take data from database //articleDatabase.getArticles()
    val storiesLiveData: LiveData<List<Story>>
    private val mutableSearchTerm = MutableLiveData<String>()

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
    }

}