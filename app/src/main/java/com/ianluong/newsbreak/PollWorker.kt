package com.ianluong.newsbreak

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story
import java.util.*

class PollWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val storyRepository = StoryRepository.get()

    override fun doWork(): Result {
        //TODO Replace placeholder PollWorker background tasks
        val stories = storyRepository.getStoriesSync()


        for (i in stories.indices) {
            val items: List<Article> =
                NewsFetcher().searchNewsQueryRequest(stories[i].title!!).execute().body()?.articles!!

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
                createNotification(stories[i], i)
            }


        }
        return Result.success()
    }

    private fun createNotification(story: Story, index: Int) {
        val intent = FollowedStoryActivity.newIntent(context)
        intent.putExtra("STORY", story)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification =
            NotificationCompat.Builder(context, story.storyId.toString())
                .setTicker("New articles added to story ${story.title}")
                .setSmallIcon(R.drawable.ic_story_update)
                .setContentTitle("Story ${story.title} updated").setContentText("Tap to view")
                .setContentIntent(pendingIntent).setAutoCancel(true)
                .build()

        notification.flags = Notification.FLAG_AUTO_CANCEL

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(index, notification)
    }
}