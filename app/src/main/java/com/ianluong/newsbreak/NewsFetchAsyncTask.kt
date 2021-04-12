package com.ianluong.newsbreak

import android.os.AsyncTask
import com.google.gson.Gson
import layout.Article
import layout.SearchResult
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class NewsFetchAsyncTask (private val q : String? = null,
                          private val newsFetchedListener: NewsFetchedListener? = null  )
    : AsyncTask<String, String, String>() {

    @Throws(IOException::class)
    private fun sendGet(url: String): String {
        val obj = URL(url)
        val con = obj.openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        val responseCode = con.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) { // connection ok
            val ins = BufferedReader(InputStreamReader(con.inputStream))
            val response = StringBuffer()

            var line: String?

            do {

                line = ins.readLine()

                if (line == null)
                    break

                response.append(line)


            } while (true)

            ins.close()
            return response.toString()
        } else {
            return ""
        }
    }


    override fun doInBackground(vararg params: String?): String {
        val date = Date()
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
        val url =
            "https://newsapi.org/v2/everything?q=$q&from=$dateString&sortBy=publishedAt&apiKey=34acee49c2054580bc2e8aa1fe0dfcb4&language=en"
        val s = sendGet(url)

        return s
    }

    override fun onPostExecute(result: String?) {
        if (result != null) {
            parseJson(result)
        }
    }

    private fun parseJson(s: String) {
        val gson = Gson()
        val result = gson.fromJson(s, SearchResult::class.java)

        if (result.status == "ok") {
            newsFetchedListener?.whenNewsFetchedSuccessfully(result.articles)
        } else {
            newsFetchedListener?.whenNewsFetchedError("Error Fetching News")
        }

    }
}

interface NewsFetchedListener {

    fun whenNewsFetchedSuccessfully ( articles : List<Article>?)

    fun whenNewsFetchedError (error: String?)
}