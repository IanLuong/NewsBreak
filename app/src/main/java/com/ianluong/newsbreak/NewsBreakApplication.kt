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
        deleteOldChannels()
    }

    private fun deleteOldChannels() {
        val notificationManager: NotificationManager =
            getSystemService(NotificationManager::class.java)
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