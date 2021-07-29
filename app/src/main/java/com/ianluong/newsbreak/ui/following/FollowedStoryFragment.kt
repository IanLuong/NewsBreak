package com.ianluong.newsbreak.ui.following

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.squareup.picasso.Picasso
import java.util.*

private const val ARG_STORY_ID = "story_id"

class FollowedStoryFragment : Fragment() {

    companion object {
        fun newInstance(storyID: String): FollowedStoryFragment {
            val args = Bundle().apply {
                putString(ARG_STORY_ID, storyID)
            }
            return FollowedStoryFragment().apply {
                arguments = args
            }
        }
    }

    private lateinit var followedStoryViewModel: FollowedStoryViewModel
    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var storyID: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followedStoryViewModel =
            ViewModelProvider(this).get(FollowedStoryViewModel::class.java)
        storyID = arguments?.getString(ARG_STORY_ID).toString()
        followedStoryViewModel.loadArticles(UUID.fromString(storyID))

        val root = inflater.inflate(R.layout.fragment_followed_story, container, false)

        articleRecyclerView = root.findViewById(R.id.followed_story_recycler_view)
        articleRecyclerView.layoutManager = LinearLayoutManager(context)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followedStoryViewModel.articlesLiveData.observe(viewLifecycleOwner, { articles ->
            articleRecyclerView.adapter = FollowedStoryAdapter(articles)
        })

    }

    private inner class FollowedStoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var article: Article

        private val articleTitle: TextView = itemView.findViewById(R.id.article_title)
        private val articleDescription: TextView =
            itemView.findViewById(R.id.article_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.article_image)
        private val articleDate: TextView = itemView.findViewById(R.id.article_date)
        private val followButton: ImageButton = itemView.findViewById(R.id.article_follow_button)
        private val readMoreButton: ImageButton =
            itemView.findViewById(R.id.article_read_more_button)

        fun bind(article: Article) {

            this.article = article

            articleTitle.text =
                getString(R.string.article_title_text, article.title, article.author)
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            articleDate.text = article.publishedAt.toString()
            followButton.visibility = GONE
            setImage()
            setReadMoreButtonListener(readMoreButton, article.url)
        }

        private fun setImage() {
            if (article.urlToImage != null) {
                Picasso.get().load(article.urlToImage).into(articleImage)
            } else {
                Picasso.get().load(R.drawable.article_placeholder).into(articleImage)
            }
        }

        private fun setReadMoreButtonListener(button: ImageButton, url: String?) {
            button.setOnClickListener {
                val website = Uri.parse(url) ?: null
                if (website != null) {
                    val intent = Intent(Intent.ACTION_VIEW, website)
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Article not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private inner class FollowedStoryAdapter(var articles: List<Article>) :
        RecyclerView.Adapter<FollowedStoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowedStoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_article, parent, false)
            return FollowedStoryHolder(view)
        }

        override fun onBindViewHolder(holder: FollowedStoryHolder, position: Int) {
            val article = articles[position]
            holder.bind(article)
        }

        override fun getItemCount(): Int {
            return articles.size
        }


    }
}