package com.ianluong.newsbreak

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class FollowedStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followed_story)

        val storyTitle = intent.getStringExtra("STORY_TITLE")
        if(title != null) {
            title =  storyTitle
        } else {
            title = getString(R.string.followed_story_placeholder)
        }
    }
}