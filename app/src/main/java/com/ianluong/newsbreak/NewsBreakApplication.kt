package com.ianluong.newsbreak

import android.app.Application
import com.ianluong.newsbreak.api.QueryPreferences

class NewsBreakApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        StoryRepository.initialize(this)
        QueryPreferences.setQuery("", this) //Resets back to default search on start
    }
}