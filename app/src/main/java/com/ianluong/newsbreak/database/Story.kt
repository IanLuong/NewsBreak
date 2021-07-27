package com.ianluong.newsbreak.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ianluong.newsbreak.api.Article
import java.util.*

@Entity
class Story {
    @PrimaryKey var id: UUID = UUID.randomUUID()
    var title: String = ""
    //var articles: ArrayList<Article> = arrayListOf()

}