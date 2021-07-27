package com.ianluong.newsbreak

import android.content.Context
import androidx.room.Room
import com.ianluong.newsbreak.database.Story
import com.ianluong.newsbreak.database.StoryDatabase
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "story-database"

class StoryRepository private constructor(context: Context) {

    private val database : StoryDatabase = Room.databaseBuilder(
        context.applicationContext,
        StoryDatabase::class.java,
        DATABASE_NAME).build()

    private val storyDao = database.storyDao()

    fun getStories(): List<Story> = storyDao.getStories()

    fun getStory(id: UUID): Story? = storyDao.getStory(id)

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