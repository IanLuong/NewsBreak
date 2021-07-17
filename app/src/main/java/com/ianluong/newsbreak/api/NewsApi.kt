package com.ianluong.newsbreak.api

import android.content.res.Resources
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    //TODO replace api key in string with call to resource
    @GET("v2/top-headlines?sources=bbc-news")
    fun fetchBBCHeadlines(): Call<NewsResult>

    @GET("v2/top-headlines?country=gb")
    fun fetchUKHeadlines(): Call<NewsResult>

    //@GET("v2/everything?q=&sources=bbc-news&apiKey=34acee49c2054580bc2e8aa1fe0dfcb4")
    //fun fetchSearch(): Call<NewsResult>

    @GET("v2/everything")
    fun fetchSearch(@Query("q") query: String): Call<NewsResult>

}