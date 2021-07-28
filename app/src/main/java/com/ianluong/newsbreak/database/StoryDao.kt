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

}