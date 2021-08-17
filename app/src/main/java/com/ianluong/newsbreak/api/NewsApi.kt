package com.ianluong.newsbreak.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines?sources=bbc-news")
    fun fetchBBCHeadlines(): Call<NewsResult>

    @GET("v2/top-headlines?country=gb")
    fun fetchUKHeadlines(): Call<NewsResult>

    @GET("v2/everything?language=en")
    fun fetchSearch(@Query("q") query: String): Call<NewsResult>

}