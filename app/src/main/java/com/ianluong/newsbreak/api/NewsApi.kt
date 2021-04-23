package com.ianluong.newsbreak.api

import android.content.res.Resources
import retrofit2.Call
import retrofit2.http.GET

interface NewsApi {
    //TODO replace api key in string with call to resource
    @GET("v2/top-headlines?sources=bbc-news&apiKey=34acee49c2054580bc2e8aa1fe0dfcb4")
    fun fetchBBCHeadlines(): Call<NewsResult>
}