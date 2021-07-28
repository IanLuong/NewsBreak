package com.ianluong.newsbreak

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FollowedStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followed_story)
        setTitle()
    }

    private fun setTitle() {
        val storyTitle = intent.getStringExtra("STORY_TITLE")
        title = if(title != null) {
            storyTitle
        } else {
            getString(R.string.followed_story_placeholder)
        }
    }
}