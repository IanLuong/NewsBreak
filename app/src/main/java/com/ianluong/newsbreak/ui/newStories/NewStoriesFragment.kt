package com.ianluong.newsbreak.ui.newStories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.work.*
import com.ianluong.newsbreak.PollWorker
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.StoryRepository
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.QueryPreferences
import com.ianluong.newsbreak.database.Story
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit

private const val DIALOG_STORY_ADD = "DialogStoryAdd"
private const val REQUEST_STORY_ADD = 0 //A constant used for the dialog request code
private const val POLL_WORK = "POLL_WORk"

class NewStoriesFragment : Fragment(), StoryAddFragment.Callbacks {

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
            Toast.makeText(context, "Refreshed latest articles", Toast.LENGTH_SHORT).show()
        }

        articleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //TODO Improve handling of searches returning no articles
                if (articleRecyclerView.isEmpty()) {
                    Toast.makeText(context, "Sorry, no articles to show", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })

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

    override fun onStoryAdded(article: Article, story: Story) {
        newStoriesViewModel.addStoryAndArticleFromDialog(article, story)
        Toast.makeText(context, "${story.title} story added", Toast.LENGTH_SHORT).show()
        createChannel(story)
    }

    private fun createChannel(story: Story) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(story.storyId.toString(),
                    "${story.title} Stories", importance)

        val notificationManager: NotificationManager =
            context?.getSystemService(NotificationManager::class.java)!!
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED).build()

        notificationManager.createNotificationChannel(channel)

        val isPolling = QueryPreferences.isPolling(requireContext())
        if(isPolling) startWork(constraints)

    }

    private fun startWork(constraints: Constraints) {
        val workRequest = PeriodicWorkRequest.Builder(
            PollWorker::class.java,
            15,
            TimeUnit.MINUTES).setConstraints(constraints).build()

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
                POLL_WORK,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest)
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

            articleTitle.text = article.title
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
                button.isPressed = true
                StoryAddFragment.newInstance(article).apply {
                    setTargetFragment(this@NewStoriesFragment, REQUEST_STORY_ADD)
                    show(this@NewStoriesFragment.requireFragmentManager(), DIALOG_STORY_ADD)
                }
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


