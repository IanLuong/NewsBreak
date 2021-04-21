package com.ianluong.newsbreak.ui.newStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NewStoriesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is New Stories Fragment"
    }
    val text: LiveData<String> = _text
}