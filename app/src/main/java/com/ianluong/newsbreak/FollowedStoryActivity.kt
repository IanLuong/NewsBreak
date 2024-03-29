package com.ianluong.newsbreak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ianluong.newsbreak.database.Story
import com.ianluong.newsbreak.ui.following.FollowedStoryFragment


class FollowedStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_followed_story)

        val story = intent.getSerializableExtra("STORY") as Story
        title = story.title
        val fragment = FollowedStoryFragment.newInstance(story)
        supportFragmentManager.beginTransaction().add(R.id.followed_story_fragment_container, fragment).commit()
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, FollowedStoryActivity::class.java)
        }
    }
}