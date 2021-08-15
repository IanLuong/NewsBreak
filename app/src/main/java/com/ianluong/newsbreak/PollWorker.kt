package com.ianluong.newsbreak

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ianluong.newsbreak.api.Article
import java.util.*

private const val TAG = "PollWorker"

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val storyRepository = StoryRepository.get()

    override fun doWork(): Result {
        //TODO Replace placeholder PollWorker background tasks
        val stories = storyRepository.getStoriesSync()

        val items: List<Article> =
            NewsFetcher().searchUKHeadlinesRequest().execute().body()?.articles!!

        for (i in stories.indices) {
            var storyUpdated = false
            for (article in items) {
                if ((article.title + article.description).contains(stories[i].title!!)) {
                    article.articleId = UUID.nameUUIDFromBytes(article.title?.toByteArray())
                    val id = storyRepository.insertArticle(article)
                    storyRepository.insertStoryArticleCrossRef(article, stories[i])
                    if (id != (-1).toLong()) storyUpdated = true
                }
            }

            if (storyUpdated) {
                val intent = MainActivity.newIntent(context)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                val notification =
                    NotificationCompat.Builder(context, stories[i].storyId.toString())
                        .setTicker("New articles added to story ${stories[i].title}")
                        .setSmallIcon(R.drawable.ic_story_update)
                        .setContentTitle("Story ${stories[i].title} updated").setContentText("Tap to view")
                        .setContentIntent(pendingIntent).setAutoCancel(true)
                        .build()

                notification.flags = Notification.FLAG_AUTO_CANCEL

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(i, notification)
            }


        }
        return Result.success()
    }
}