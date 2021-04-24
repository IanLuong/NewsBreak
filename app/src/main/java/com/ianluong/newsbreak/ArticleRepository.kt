package com.ianluong.newsbreak

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.ArticleDatabase
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "article-database"

class ArticleRepository private constructor(context: Context) {

    private val database: ArticleDatabase = Room.databaseBuilder(
        context.applicationContext,
        ArticleDatabase::class.java,
        DATABASE_NAME).build()

    private val articleDao = database.articleDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getArticles(): LiveData<List<Article>> = articleDao.getArticles()

    fun getArticle(id: UUID): LiveData<Article?> = articleDao.getArticle(id)

    fun updateArticle(article: Article) {
        executor.execute() {
            articleDao.updateArticle(article)
        }
    }

    fun insertArticle(article: Article) {
        executor.execute() {
            articleDao.insertArticle(article)
        }
    }

    companion object {
        private var INSTANCE: ArticleRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = ArticleRepository(context)
            }
        }

        fun get(): ArticleRepository {
            return INSTANCE ?:
            throw IllegalStateException("ArticleRepository not initialized")
        }
    }
}