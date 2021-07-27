package com.ianluong.newsbreak.database

import androidx.room.Dao
import androidx.room.Query
import java.util.*

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getStories(): List<Story>

    @Query("SELECT * FROM story WHERE id=(:id)")
    fun getStory(id: UUID): Story?
}