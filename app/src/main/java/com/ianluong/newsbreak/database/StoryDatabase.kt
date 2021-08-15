package com.ianluong.newsbreak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ianluong.newsbreak.api.Article

@Database(entities = [Story::class, Article::class, StoryArticleCrossRef::class], version=6)
@TypeConverters(StoryTypeConverters::class)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDao(): StoryDao
}