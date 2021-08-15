package com.ianluong.newsbreak.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ianluong.newsbreak.api.Article
import java.util.*

@Dao
interface StoryDao {
    @Query("SELECT * FROM story WHERE storyId=(:storyId)")
    fun getStory(storyId: UUID): LiveData<Story?>
    @Query("SELECT * FROM story")
    fun getStories(): LiveData<List<Story>>
    @Query("SELECT * FROM story")
    fun getStoriesSync(): List<Story>

    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>
    @Query("SELECT * FROM article WHERE articleId=(:articleId)")
    fun getArticle(articleId: UUID): LiveData<Article?>


    @Update
    fun updateStory(story: Story)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertStory(story: Story)

    @Query("DELETE FROM story WHERE storyId=(:storyId)")
    fun deleteStory(storyId: UUID)


    @Update
    fun updateArticle(article: Article)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArticle(article: Article): Long

    @Transaction
    @Query("SELECT * FROM Story")
    fun getStoriesWithArticles(): LiveData<List<StoryWithArticles>>

    @Transaction
    @Query("SELECT * FROM Article")
    fun getArticlesWithStories(): List<ArticleWithStories>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSoryArticleCrossRef(storyArticleCrossRef: StoryArticleCrossRef)

    //TODO Change delete to work with many-to-many
    @Transaction
    @Query("DELETE FROM storyarticlecrossref WHERE storyId=(:storyId); ")
    fun deleteStoryWithArticles(storyId: UUID)

    //TODO Change delete to work with many-to-many
    @Query("DELETE FROM article WHERE articleId NOT IN " +
            "(SELECT f.articleId FROM storyarticlecrossref f)")
    fun deleteArticles()

    @Transaction
    @Query("SELECT * FROM Story WHERE storyId =(:storyId)")
    fun getStoryWithArticles(storyId: UUID): StoryWithArticles

    @Transaction
    @Query("SELECT * FROM Article WHERE articleId =(:articleId)")
    fun getArticleWithStories(articleId: UUID): List<ArticleWithStories>
}