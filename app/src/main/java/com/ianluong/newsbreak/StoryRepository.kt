package com.ianluong.newsbreak

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story
import com.ianluong.newsbreak.database.StoryArticleCrossRef
import com.ianluong.newsbreak.database.StoryDatabase
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors

private const val DATABASE_NAME = "story-database"

class StoryRepository private constructor(context: Context) {

    private val database : StoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        StoryDatabase::class.java,
        DATABASE_NAME).fallbackToDestructiveMigration().build()

    private val storyDao = database.storyDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getStories(): LiveData<List<Story>> = storyDao.getStories()

    //Used to update the notification channels on startup
    fun getStoriesSync(): List<Story> {
        val callable = Callable {storyDao.getStoriesSync()}
        val reply = executor.submit(callable)
        return reply.get()
    }

    fun getStory(id: UUID): LiveData<Story?> = storyDao.getStory(id)

    fun updateStory(story: Story) {
        executor.execute {
            storyDao.updateStory(story)
        }
    }

    fun insertStory(story: Story) {
        executor.execute {
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

    fun insertArticle(article: Article): Long {
        val callable = Callable { storyDao.insertArticle(article) }
        val reply = executor.submit(callable)
        return reply.get()
    }

    fun insertStoryArticleCrossRef(article: Article, story: Story) {
        executor.execute() {
            val storyArticle = StoryArticleCrossRef(story.storyId, article.articleId)
            storyDao.insertSoryArticleCrossRef(storyArticle)
        }
    }

    fun getStoryWithArticles(storyId: UUID): MutableLiveData<List<Article>> {
        val callable = Callable {storyDao.getStoryWithArticles(storyId)}
        val reply = executor.submit(callable)
        val bob: MutableLiveData<List<Article>> by lazy {
            MutableLiveData<List<Article>>(reply.get().articles)
        }
        return bob
    }

    fun deleteStoryAndArticles(story: Story) {
        executor.execute() {
            storyDao.deleteStory(story.storyId)
            storyDao.deleteStoryWithArticles(story.storyId)
            storyDao.deleteArticles()
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