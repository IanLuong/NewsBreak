package com.ianluong.newsbreak

import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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
        val items: List<Article> = NewsFetcher().searchNewsQueryRequest("Scarlett").execute().body()?.articles!!
        for (article in items) {
         if ((article.title + article.description).contains("simultaneous")) {
             article.storyID = UUID.fromString("1020c7e8-dd58-48d0-9824-cc7c9031f3e2")
             storyRepository.insertArticle(article)

             val intent = MainActivity.newIntent(context)
             val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

             val notification =
                 NotificationCompat.Builder(context, "1020c7e8-dd58-48d0-9824-cc7c9031f3e2")
                     .setTicker("New Articles Added").setSmallIcon(R.drawable.ic_launcher_foreground)
                     .setContentTitle("New Articles Added").setContentText("New Articles Added")
                     .setContentIntent(pendingIntent).setAutoCancel(true)
                     .build()

             val notificationManager = NotificationManagerCompat.from(context)
             notificationManager.notify(0, notification)
             }
        }
        return Result.success()
    }
}