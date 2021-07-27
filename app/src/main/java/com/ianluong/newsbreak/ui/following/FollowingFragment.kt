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
import com.ianluong.newsbreak.database.Story
import com.squareup.picasso.Picasso

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var followingRecyclerView: RecyclerView

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
        followingViewModel.storiesLiveData.observe(viewLifecycleOwner, { stories ->
            followingRecyclerView.adapter = FollowingAdapter(stories)
        })

    }

    companion object {
        fun newInstance(): FollowingFragment {
            return FollowingFragment()
        }
    }

    //TODO Fix the binding
    private inner class FollowingHolder(view: View): RecyclerView.ViewHolder(view) {

        private lateinit var story: Story

        private val storyTitle: TextView = itemView.findViewById(R.id.following_title)

        fun bind(story: Story) {

            this.story = story

            storyTitle.text =
                getString(R.string.article_title_text, story.title, story.id)
            storyTitle.setTypeface(null, Typeface.BOLD)
        }

    }

    private inner class FollowingAdapter(var stories: List<Story>) :
        RecyclerView.Adapter<FollowingHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingHolder {
            val view = layoutInflater.inflate(R.layout.list_item_following, parent, false)
            return FollowingHolder(view)
        }

        override fun onBindViewHolder(holder: FollowingHolder, position: Int) {
            val story = stories[position]
            holder.bind(story)
        }

        override fun getItemCount(): Int {
            return stories.size
        }


    }
}

