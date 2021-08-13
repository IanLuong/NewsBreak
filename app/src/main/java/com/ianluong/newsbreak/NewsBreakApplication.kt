package com.ianluong.newsbreak

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.ianluong.newsbreak.api.QueryPreferences

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
    }

    //TODO Move Channel creation to somewhere after adding a story, rather than when app reopened
    private fun buildChannels(): List<NotificationChannel> {
        val stories = StoryRepository.get().getStoriesSync()
        val channels = mutableListOf<NotificationChannel>()
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        if(stories.isNotEmpty()) {
            for (element in stories) {
                val channel = NotificationChannel(element.id.toString(),
                    "${element.title} Stories",
                    importance)
                channels.add(channel)
            }
        }
        return channels
    }

    private fun deleteOldChannels(notificationManager: NotificationManager) {
        val stories = StoryRepository.get().getStoriesSync()
        val storyIDs = stories.map{it.id.toString()}
        val currentChannels = notificationManager.notificationChannels
        for(channel in currentChannels) {
            if(channel.id !in storyIDs) {
                notificationManager.deleteNotificationChannel(channel.id)
            }
        }
    }

}