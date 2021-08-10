package com.ianluong.newsbreak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ianluong.newsbreak.api.Article

@Database(entities = [Story::class, Article::class], version=3)
@TypeConverters(StoryTypeConverters::class)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDao(): StoryDao
}