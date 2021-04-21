package com.ianluong.newsbreak.ui.newStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ianluong.newsbreak.Article
import java.util.*

class NewStoriesViewModel : ViewModel() {

    val articles = mutableListOf<Article>()

    init {
        for(i in 0..19) {
            val article = Article()
            article.title = "Article number " + i
            var description = "Description " + i
            var url = "https://www.bbc.co.uk/news/uk-politics-56832486"
            var urlToImage = "https://ichef.bbci.co.uk/news/976/cpsprodpb/CAAB/production/_118138815_dyson2_johnson_texts2x640-nc.png"
            var publishedAt : Date? = Date("11/11/11")
            articles.add(article)
        }
    }
}