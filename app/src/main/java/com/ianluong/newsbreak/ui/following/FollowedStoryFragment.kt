package com.ianluong.newsbreak.ui.following

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ianluong.newsbreak.R

class FollowedStoryFragment : Fragment() {

    companion object {
        fun newInstance() = FollowedStoryFragment()
    }

    private lateinit var viewModel: FollowedStoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.followed_story_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FollowedStoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}