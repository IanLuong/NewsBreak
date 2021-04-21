package com.ianluong.newsbreak.ui.following

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FollowingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Following Fragment"
    }
    val text: LiveData<String> = _text
}