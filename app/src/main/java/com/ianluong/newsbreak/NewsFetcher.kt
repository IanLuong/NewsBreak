package com.ianluong.newsbreak

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ianluong.newsbreak.api.Article
import com.ianluong.newsbreak.api.ArticleInterceptor
import com.ianluong.newsbreak.api.NewsApi
import com.ianluong.newsbreak.api.NewsResult
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "NewsFetcher"

class NewsFetcher {

    private val newsApi: NewsApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(ArticleInterceptor())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        newsApi = retrofit.create(NewsApi::class.java)
    }

    fun fetchNews(query: String): LiveData<List<Article>> {
        val responseLiveData: MutableLiveData<List<Article>> = MutableLiveData()

        val newsRequest: Call<NewsResult> =  when(query) {
            //TODO Refactor headlines in search
            "BBCHeadlines" -> newsApi.fetchBBCHeadlines()
            "UKHeadlines" -> newsApi.fetchUKHeadlines()
            else -> newsApi.fetchSearch(query)
        }
                newsRequest.enqueue(object: Callback<NewsResult> {
                override fun onResponse(call: Call<NewsResult>, response: Response<NewsResult>) {
                    Log.d(TAG, "RESPONSE RECEIVED")
                    val newsResponse : NewsResult? = response.body()
                    val articleResponse = newsResponse?.articles

                    responseLiveData.value = articleResponse
                }

                override fun onFailure(call: Call<NewsResult>, t: Throwable) {
                    Log.e(TAG, "ERROR FETCHING NEWS", t)
                }

            })

        return responseLiveData
    }
}