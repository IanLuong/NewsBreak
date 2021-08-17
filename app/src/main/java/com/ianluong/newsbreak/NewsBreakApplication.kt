package com.ianluong.newsbreak

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import androidx.work.*
import com.ianluong.newsbreak.api.QueryPreferences
import java.util.concurrent.TimeUnit

class NewsBreakApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        StoryRepository.initialize(this)

        //This resets current query back to default search on app start
        QueryPreferences.setQuery("", this)

        val channels = buildChannels()
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java)
        if (channels.isNotEmpty()) {
            notificationManager.createNotificationChannels(channels)
        }
        deleteOldChannels(notificationManager)

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED).build()

        val workRequest = PeriodicWorkRequest.Builder(
            PollWorker::class.java,
            15,
            TimeUnit.MINUTES).setConstraints(constraints).build()

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork("updateStories",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest)
    }

    private fun buildChannels(): List<NotificationChannel> {
        val stories = StoryRepository.get().getStoriesSync()
        val channels = mutableListOf<NotificationChannel>()
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if(stories.isNotEmpty()) {
            for (element in stories) {
                val channel = NotificationChannel(element.storyId.toString(),
                    "${element.title} Stories",
                    importance)
                channels.add(channel)
            }
        }
        return channels
    }

    private fun deleteOldChannels(notificationManager: NotificationManager) {
        val stories = StoryRepository.get().getStoriesSync()
        val storyIDs = stories.map{it.storyId.toString()}
        val currentChannels = notificationManager.notificationChannels
        for(channel in currentChannels) {
            if(channel.id !in storyIDs) {
                notificationManager.deleteNotificationChannel(channel.id)
            }
        }
    }

}