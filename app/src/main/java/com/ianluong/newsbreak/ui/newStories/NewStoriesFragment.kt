package com.ianluong.newsbreak.ui.newStories

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.NewsFetcher
import com.ianluong.newsbreak.R

private const val TAG = "NewStoriesFragment"

class NewStoriesFragment : Fragment() {

    private lateinit var newStoriesViewModel: NewStoriesViewModel
    private lateinit var articleRecyclerView: RecyclerView
    private var adapter: NewStoryHolder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsLiveData: LiveData<List<Article>> = NewsFetcher().fetchBBCHeadlines()
        newsLiveData.observe(this, Observer {articles ->
            Log.d(TAG, "Response Received: $articles")
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStoriesViewModel =
            ViewModelProvider(this).get(NewStoriesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_new_stories, container, false)

        articleRecyclerView = root.findViewById(R.id.article_recycler_view)
        articleRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return root
    }

    private fun updateUI() {
        val articles = newStoriesViewModel.articles
        val adapter = NewStoryAdapter(articles)
        articleRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): NewStoriesFragment {
            return NewStoriesFragment()
        }
    }

    private inner class NewStoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var article: Article

        private val articleTitle: TextView = itemView.findViewById(R.id.list_item_article_title)
        private val articleDescription: TextView = itemView.findViewById(R.id.list_item_article_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.list_item_article_image)
        private val followButton: ImageButton = itemView.findViewById(R.id.follow_button)
        private val readMoreButton: Button = itemView.findViewById(R.id.read_more_button)

        fun bind(article: Article) {
            articleTitle.text = article.title
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            //articleImage = article.title TODO add image binding functionality
            //followButton.text = article.title TODO add follow button functionality
            setReadMoreButtonListener(readMoreButton, article.url)
        }

        fun setReadMoreButtonListener(button: Button, url: String?) {
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

    private inner class NewStoryAdapter(var articles: List<Article>) :
        RecyclerView.Adapter<NewStoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewStoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_story, parent, false)
            return NewStoryHolder(view)
        }

        override fun onBindViewHolder(holder: NewStoryHolder, position: Int) {
            val article = articles[position]
            holder.bind(article)
        }

        override fun getItemCount(): Int {
            return articles.size
        }

        }
    }


