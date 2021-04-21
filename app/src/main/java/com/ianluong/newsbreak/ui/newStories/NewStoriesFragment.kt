package com.ianluong.newsbreak.ui.newStories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ianluong.newsbreak.R

class NewStoriesFragment : Fragment() {

    private lateinit var newStoriesViewModel: NewStoriesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        newStoriesViewModel =
            ViewModelProvider(this).get(NewStoriesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_new_stories, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        newStoriesViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}