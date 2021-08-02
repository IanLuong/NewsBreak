package com.ianluong.newsbreak.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val API_KEY = "34acee49c2054580bc2e8aa1fe0dfcb4"

class ArticleInterceptor : Interceptor {
//TODO Refactor API Key
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val newUrl: HttpUrl = originalRequest.url().newBuilder()
            //TODO Fix Sources url intercept
            //.addQueryParameter("sources", "bbc-news")
            .addQueryParameter("apiKey", API_KEY)
            .build()

        val newRequest: Request = originalRequest.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest)
    }

}