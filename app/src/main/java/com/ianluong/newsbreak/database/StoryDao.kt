package com.ianluong.newsbreak.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ianluong.newsbreak.api.Article
import java.util.*

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getStories(): LiveData<List<Story>>

    @Query("SELECT * FROM story WHERE id=(:id)")
    fun getStory(id: UUID): LiveData<Story?>

    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM article WHERE id=(:id)")
    fun getArticle(id: UUID): LiveData<Article?>

    @Query("SELECT * FROM article WHERE storyID=(:storyID)")
    fun getArticlesWithStoryID(storyID: UUID): LiveData<List<Article>>

    @Transaction
    @Query("SELECT * FROM story")
    fun getStoriesWithArticles(): LiveData<List<StoryWithArticles>>

    @Transaction
    @Query("SELECT * FROM story WHERE id=(:id)")
    fun getStoryWithArticles(id: UUID): LiveData<StoryWithArticles>

    @Update
    fun updateStory(story: Story)

    @Insert
    fun insertStory(story: Story)

    @Update
    fun updateArticle(article: Article)

    @Insert
    fun insertArticle(article: Article)
}