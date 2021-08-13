package com.ianluong.newsbreak

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.ianluong.newsbreak.api.QueryPreferences

class NewsBreakApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        StoryRepository.initialize(this)

        //This resets current query back to default search on app start
        QueryPreferences.setQuery("", this)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = buildChannels()
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            if (channels.isNotEmpty()) {
                notificationManager.createNotificationChannels(channels)
            }
            notificationManager.deleteNotificationChannel("news_poll")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
}