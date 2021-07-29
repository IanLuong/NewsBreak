package com.ianluong.newsbreak

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ianluong.newsbreak.ui.following.FollowedStoryFragment


class FollowedStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followed_story)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.followed_story_fragment_container)
        if (currentFragment == null) {
            val id = intent.getStringExtra("STORY_ID")
            val fragment = FollowedStoryFragment.newInstance(id!!)
            supportFragmentManager.beginTransaction().add(R.id.followed_story_fragment_container, fragment).commit()
        }

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