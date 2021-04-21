package com.ianluong.newsbreak.ui.newStories

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ianluong.newsbreak.Article
import com.ianluong.newsbreak.R

private const val TAG = "CrimeListFragment"

class NewStoriesFragment : Fragment() {

    private lateinit var newStoriesViewModel: NewStoriesViewModel

    private lateinit var article: Article
    private lateinit var articleTitle: TextView
    private lateinit var articleDescription: TextView
    private lateinit var articleImage: ImageView
    private lateinit var followButton: Button
    private lateinit var readMoreButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStoriesViewModel =
            ViewModelProvider(this).get(NewStoriesViewModel::class.java)
        Log.d(TAG, "Total crimes ${newStoriesViewModel.articles.size}")

        val root = inflater.inflate(R.layout.fragment_new_stories, container, false)

        articleTitle = root.findViewById(R.id.article_title_textview)
        articleDescription = root.findViewById(R.id.article_description_textview)
        articleImage = root.findViewById(R.id.article_image_imageview)
        followButton = root.findViewById(R.id.follow_button)
        readMoreButton = root.findViewById(R.id.read_more_button)

        return root
    }

    override fun onStart() {
        super.onStart()

        readMoreButton.setOnClickListener() {
            val website = Uri.parse("https://google.com")
            val intent = Intent(Intent.ACTION_VIEW, website)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Log.d("ArticleFragment.kt", "Not a URL")
            }
        }
    }

    companion object {
        fun newInstance(): NewStoriesFragment {
            return NewStoriesFragment()
        }
    }
}


