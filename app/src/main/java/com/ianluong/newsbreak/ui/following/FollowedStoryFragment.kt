package com.ianluong.newsbreak.ui.following

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ianluong.newsbreak.PollWorker
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.database.Story
import com.squareup.picasso.Picasso
import java.util.*
import java.util.concurrent.TimeUnit

private const val ARG_STORY = "story"
private const val DIALOG_STORY_DELETE = "DialogStoryDelete"
private const val REQUEST_DELETE_STORY = 2


class FollowedStoryFragment : Fragment(), StoryDeleteFragment.Callbacks {

    private lateinit var followedStoryViewModel: FollowedStoryViewModel
    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var story: Story

    companion object {
        fun newInstance(story: Story): FollowedStoryFragment {
            val args = Bundle().apply {
                putSerializable(ARG_STORY, story)
            }
            return FollowedStoryFragment().apply {
                arguments = args
            }
        }
    }

    override fun onStoryDeleted(story: Story) {
        followedStoryViewModel.deleteStoryAndArticles(story)
        activity?.finish()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followedStoryViewModel =
            ViewModelProvider(this).get(FollowedStoryViewModel::class.java)
        story = arguments?.getSerializable(ARG_STORY) as Story
        followedStoryViewModel.loadArticles(story)

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
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_followed_story, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //TODO Replace deprecated functions
        return if (item.itemId == R.id.menu_item_delete_story) {
            StoryDeleteFragment.newInstance(story).apply {
                setTargetFragment(this@FollowedStoryFragment, REQUEST_DELETE_STORY)
                show(this@FollowedStoryFragment.requireFragmentManager(), DIALOG_STORY_DELETE)
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
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

            articleTitle.text = article.title
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