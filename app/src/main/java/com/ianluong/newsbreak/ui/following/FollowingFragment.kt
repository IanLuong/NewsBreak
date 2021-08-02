package com.ianluong.newsbreak.ui.following

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ianluong.newsbreak.FollowedStoryActivity
import com.ianluong.newsbreak.R
import com.ianluong.newsbreak.database.Story
import java.util.*

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
        followingViewModel.updateStory()
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

    private inner class FollowingHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var story: Story

        private val storyTitle: TextView = itemView.findViewById(R.id.following_title)
        private val moreButton: Button = itemView.findViewById(R.id.following_button)

        fun bind(story: Story) {

            this.story = story
            storyTitle.text = story.title

            moreButton.setOnClickListener {
                val intent = Intent(context, FollowedStoryActivity::class.java)
                intent.putExtra("STORY", this.story)
                startActivity(intent)
            }
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

