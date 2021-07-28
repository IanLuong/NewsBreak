package com.ianluong.newsbreak.ui.following

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.squareup.picasso.Picasso

class FollowedStoryFragment : Fragment() {

    companion object {
        fun newInstance() = FollowedStoryFragment()
    }

    private lateinit var followedStoryViewModel: FollowedStoryViewModel
    private lateinit var articleRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followedStoryViewModel =
            ViewModelProvider(this).get(FollowedStoryViewModel::class.java)

        val root =  inflater.inflate(R.layout.fragment_followed_story, container, false)

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

        private val articleTitle: TextView = itemView.findViewById(R.id.new_stories_title)
        private val articleDescription: TextView =
            itemView.findViewById(R.id.new_stories_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.new_stories_image)
        private val articleDate: TextView = itemView.findViewById(R.id.new_stories_date)
        private val readMoreButton: ImageButton =
            itemView.findViewById(R.id.new_stories_read_more_button)

        fun bind(article: Article) {

            this.article = article

            articleTitle.text =
                getString(R.string.article_title_text, article.title, article.author)
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            articleDate.text = article.publishedAt.toString()
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
            val view = layoutInflater.inflate(R.layout.list_item_story, parent, false)
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