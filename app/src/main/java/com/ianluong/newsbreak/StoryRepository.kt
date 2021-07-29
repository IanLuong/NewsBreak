package com.ianluong.newsbreak

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story
import com.ianluong.newsbreak.database.StoryDatabase
import com.ianluong.newsbreak.database.StoryWithArticles
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "story-database"

class StoryRepository private constructor(context: Context) {

    private val database : StoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        StoryDatabase::class.java,
        DATABASE_NAME).build()

    private val storyDao = database.storyDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getStories(): LiveData<List<Story>> = storyDao.getStories()

    fun getStory(id: UUID): LiveData<Story?> = storyDao.getStory(id)

    fun getStoriesWithArticles(): LiveData<List<StoryWithArticles>> = storyDao.getStoriesWithArticles()

    fun getStoryWithArticles(id: UUID): LiveData<StoryWithArticles> = storyDao.getStoryWithArticles(id)

    fun getArticlesWithStoryID(storyID: UUID): LiveData<List<Article>> = storyDao.getArticlesWithStoryID(storyID)

    fun updateStory(story: Story) {
        executor.execute() {
            storyDao.updateStory(story)
        }
    }

    fun insertStory(story: Story) {
        executor.execute() {
            storyDao.insertStory(story)
        }
    }

    fun getArticles(): LiveData<List<Article>> = storyDao.getArticles()

    fun getArticle(id: UUID): LiveData<Article?> = storyDao.getArticle(id)

    fun updateArticle(article: Article) {
        executor.execute() {
            storyDao.updateArticle(article)
        }
    }

    fun insertArticle(article: Article) {
        executor.execute() {
            storyDao.insertArticle(article)
        }
    }

    companion object {
        private var INSTANCE: StoryRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = StoryRepository(context)
            }
        }

        fun get(): StoryRepository {
            return INSTANCE ?: throw IllegalStateException("StoryRepository not initialized")
        }
    }
}