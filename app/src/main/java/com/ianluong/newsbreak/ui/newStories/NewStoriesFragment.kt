package com.ianluong.newsbreak.ui.newStories

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.R
import com.squareup.picasso.Picasso

private const val TAG = "NewStoriesFragment"

class NewStoriesFragment : Fragment() {

    private lateinit var newStoriesViewModel: NewStoriesViewModel
    private lateinit var articleRecyclerView: RecyclerView
    private var adapter: NewStoryHolder? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStoriesViewModel =
            ViewModelProvider(this).get(NewStoriesViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_new_stories, container, false)

        articleRecyclerView = root.findViewById(R.id.new_stories_recycler_view)
        articleRecyclerView.layoutManager = LinearLayoutManager(context)

        setHasOptionsMenu(true)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newStoriesViewModel.articlesLiveData.observe(viewLifecycleOwner, Observer {articles ->
            articleRecyclerView.adapter = NewStoryAdapter(articles)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_new_stories, menu)

        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    newStoriesViewModel.fetchSearch(query)
                    searchView.clearFocus()
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

        private val articleTitle: TextView = itemView.findViewById(R.id.new_stories_title)
        private val articleDescription: TextView = itemView.findViewById(R.id.new_stories_description)
        private val articleImage: ImageView = itemView.findViewById(R.id.new_stories_image)
        private val articleDate: TextView = itemView.findViewById(R.id.new_stories_date)
        private val followButton: ImageButton = itemView.findViewById(R.id.new_stories_follow_button)
        private val readMoreButton: ImageButton = itemView.findViewById(R.id.new_stories_read_more_button)

        fun bind(article: Article) {

            this.article = article

            articleTitle.text = getString(R.string.article_title_text, article.title, article.author)
            articleTitle.setTypeface(null, Typeface.BOLD)
            articleDescription.text = article.description
            articleDate.text = article.publishedAt.toString()
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


