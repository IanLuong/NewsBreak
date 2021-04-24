package com.ianluong.newsbreak

import android.app.Application

class NewsBreakApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ArticleRepository.initialize(this)


    }
}