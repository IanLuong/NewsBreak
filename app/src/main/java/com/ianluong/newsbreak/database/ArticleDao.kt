package com.ianluong.newsbreak.database

import androidx.room.Dao
import androidx.room.Query
import com.ianluong.newsbreak.api.Article
import java.util.*

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getCrimes(): List<Article>

    @Query("SELECT * FROM article WHERE id=(:id)")
    fun getCrime(id: UUID): Article?
}