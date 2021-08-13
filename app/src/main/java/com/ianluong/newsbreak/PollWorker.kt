package com.ianluong.newsbreak

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ianluong.newsbreak.api.Article
import java.util.*

private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {

    private val storyRepository = StoryRepository.get()

    override fun doWork(): Result {
        //TODO Replace placeholder PollWorker background tasks
        Log.i(TAG, "Work request triggered")
        val items: List<Article> = NewsFetcher().searchUKHeadlinesRequest().execute().body()?.articles!!
        for (article in items) {
         if ((article.title + article.description).contains("Widow")) {
                article.storyID = UUID.fromString("1020c7e8-dd58-48d0-9824-cc7c9031f3e2")
                storyRepository.insertArticle(article)
            }
        }
        return Result.success()
    }
}