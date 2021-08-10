package com.ianluong.newsbreak.ui.newStories

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.Story
import java.util.*

class NewStoriesViewModel(private val app: Application) : AndroidViewModel(app) {

    private val storyRepository = StoryRepository.get()

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

    fun insertArticle(article: Article) {
        storyRepository.insertArticle(article)
    }

    fun insertStory(story: Story) {
        storyRepository.insertStory(story)
    }

    fun addStoryAndArticleFromDialog(article: Article, story: Story) {
        article.id = UUID.randomUUID()
        article.storyID = story.id
        insertArticle(article)
        insertStory(story)
    }
}