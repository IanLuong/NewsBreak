package com.ianluong.newsbreak.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ianluong.newsbreak.R

class FollowingFragment : Fragment() {

    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        followingViewModel =
            ViewModelProvider(this).get(FollowingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_following, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        followingViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}