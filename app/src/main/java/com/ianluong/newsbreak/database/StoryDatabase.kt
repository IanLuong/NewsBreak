package com.ianluong.newsbreak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Story::class], version=1)
@TypeConverters(StoryTypeConverters::class)
abstract class StoryDatabase: RoomDatabase() {

    abstract fun storyDao(): StoryDao
}