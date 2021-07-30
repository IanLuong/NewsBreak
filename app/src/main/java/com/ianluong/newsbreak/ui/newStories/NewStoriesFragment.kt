package com.ianluong.newsbreak.ui.newStories

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.squareup.picasso.Picasso
import java.util.*

private const val TAG = "NewStoriesFragment"

class NewStoriesFragment : Fragment() {

    private lateinit var newStoriesViewModel: NewStoriesViewModel
    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var articleSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStoriesViewModel =
            ViewModelProvider(this).get(NewStoriesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_new_stories, container, false)

        articleSwipeRefreshLayout = root.findViewById(R.id.new_stories_swipe_refresh_layout)
        articleRecyclerView = root.findViewById(R.id.new_stories_recycler_view)
        articleRecyclerView.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newStoriesViewModel.articlesLiveData.observe(viewLifecycleOwner, { articles ->
            articleRecyclerView.adapter = NewStoryAdapter(articles)
        })

        articleSwipeRefreshLayout.setOnRefreshListener{
            newStoriesViewModel.fetchSearch(newStoriesViewModel.searchTerm)
            articleSwipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_new_stories, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    newStoriesViewModel.fetchSearch(query)
                    searchView.onActionViewCollapsed()

                    if (articleRecyclerView.adapter?.itemCount == 0) {
                        Toast.makeText(context, "Sorry, no articles were found", Toast.LENGTH_SHORT)
                            .show()
                    }

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            setOnSearchClickListener {
                searchView.setQuery(newStoriesViewModel.searchTerm, false)
            }

            setOnCloseListener {
                newStoriesViewModel.fetchUKHeadlines()
                searchView.onActionViewCollapsed()
                true
            }
        }
    }

    companion object {
        fun newInstance(): NewStoriesFragment {
            return NewStoriesFragment()
        }
    }

    private inner class NewStoryHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var article: Article

        private val articleTitle: TextView = itemView.findViewById(R.id.article_title)
        private val articleDescription: TextView =
            itemView.findViewById(R.id.article_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.article_image)
        private val articleDate: TextView = itemView.findViewById(R.id.article_date)
        private val followButton: ImageButton =
            itemView.findViewById(R.id.article_follow_button)
        private val readMoreButton: ImageButton =
            itemView.findViewById(R.id.article_read_more_button)

        fun bind(article: Article) {

            this.article = article

            articleTitle.text =
                getString(R.string.article_title_text, article.title, article.author)
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            articleDate.text = article.publishedAt.toString()
            setImage()
            setFollowButtonListener(followButton, article)
            setReadMoreButtonListener(readMoreButton, article.url)
        }

        private fun setImage() {
            if (article.urlToImage == null || article.urlToImage!!.isEmpty()) {
                Picasso.get().load(R.drawable.article_placeholder).into(articleImage)
            } else{
                Picasso.get().load(article.urlToImage).into(articleImage)
            }
        }

        private fun setFollowButtonListener(button: ImageButton, article: Article) {
            button.setOnClickListener {
                article.storyID = UUID.fromString("4b5fcb60-f0fa-4928-8f4a-d0da75d489c4")
                newStoriesViewModel.insertArticle(article)
                button.isPressed = true
                Toast.makeText(context, "Article added", Toast.LENGTH_SHORT).show()
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

    private inner class NewStoryAdapter(var articles: List<Article>) :
        RecyclerView.Adapter<NewStoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewStoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_article, parent, false)
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


