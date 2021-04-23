package com.ianluong.newsbreak.ui.newStories

import androidx.lifecycle.ViewModel
import com.ianluong.newsbreak.api.Article
import java.util.*

class NewStoriesViewModel : ViewModel() {

    val articles = mutableListOf<Article>()

    init {
        for(i in 0..99) {
            val article = Article()
            article.title = "Article number " + i
            article.description = "Description number " + i
            article.url = "https://www.bbc.co.uk/news/uk-politics-56832486"
            article.urlToImage = "https://ichef.bbci.co.uk/news/976/cpsprodpb/CAAB/production/_118138815_dyson2_johnson_texts2x640-nc.png"
            article.publishedAt = Date("11/11/11")
            articles.add(article)
        }
    }
}