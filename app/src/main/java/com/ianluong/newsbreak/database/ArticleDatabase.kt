package com.ianluong.newsbreak.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ianluong.newsbreak.api.Article

@Database(entities = [Article::class], version=1)
@TypeConverters(ArticleTypeConverters::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

}