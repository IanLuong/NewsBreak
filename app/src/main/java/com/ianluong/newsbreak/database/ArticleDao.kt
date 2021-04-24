package com.ianluong.newsbreak.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ianluong.newsbreak.api.Article
import java.util.*

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM article WHERE id=(:id)")
    fun getArticle(id: UUID): LiveData<Article?>

    @Update
    fun updateArticle(article: Article)

    @Insert
    fun insertArticle(article: Article)
}