package com.ianluong.newsbreak.ui.following

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.squareup.picasso.Picasso

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var followingRecyclerView: RecyclerView
    private var adapter: FollowingHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followingViewModel =
            ViewModelProvider(this).get(FollowingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_following, container, false)

        followingRecyclerView = root.findViewById(R.id.following_recycler_view)
        followingRecyclerView.layoutManager = LinearLayoutManager(context)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingViewModel.articles.observe(viewLifecycleOwner, Observer {articles ->
            followingRecyclerView.adapter = FollowingAdapter(articles)
        })

    }

    companion object {
        fun newInstance(): FollowingFragment {
            return FollowingFragment()
        }
    }

    //TODO Fix the binding
    private inner class FollowingHolder(view: View): RecyclerView.ViewHolder(view) {

        private lateinit var article: Article

        private val articleTitle: TextView = itemView.findViewById(R.id.following_title)
        private val articleDescription: TextView = itemView.findViewById(R.id.following_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.following_image)
        private val followButton: ImageButton = itemView.findViewById(R.id.following_follow_button)
        private val readMoreButton: ImageButton = itemView.findViewById(R.id.following_read_more_button)

        fun bind(article: Article) {

            this.article = article

            articleTitle.text = getString(R.string.article_title_text, article.title, article.author)
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            setImage()
            //followButton.text = article.title TODO add follow button functionality
            setReadMoreButtonListener(readMoreButton, article.url)
        }

        private fun setImage() {
            if(article.urlToImage != null) {
                Picasso.get().load(article.urlToImage).into(articleImage)
            }
            else {
                Picasso.get().load(R.drawable.article_placeholder).into(articleImage)
            }
        }

        private fun setReadMoreButtonListener(button: ImageButton, url: String?) {
            button.setOnClickListener() {
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

    private inner class FollowingAdapter(var articles: List<Article>) :
        RecyclerView.Adapter<FollowingHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_following, parent, false)
            return FollowingHolder(view)
        }

        override fun onBindViewHolder(holder: FollowingHolder, position: Int) {
            val article = articles[position]
            holder.bind(article)
        }

        override fun getItemCount(): Int {
            return articles.size
        }


    }
}

