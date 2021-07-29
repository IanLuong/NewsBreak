package com.ianluong.newsbreak.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class Article {
    @PrimaryKey
    var id: UUID = UUID.randomUUID()
    var title: String? = null
    var description: String? = null
    var url: String? = null
    var urlToImage: String? = null
    var publishedAt: Date? = null
    var author: String? = null

    var storyID: UUID? = null

}

