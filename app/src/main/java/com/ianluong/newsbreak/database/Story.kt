package com.ianluong.newsbreak.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ianluong.newsbreak.api.Article
import java.util.*

@Entity
class Story {
    @PrimaryKey var id: UUID = UUID.randomUUID()
    var title: String = ""

}

data class StoryWithArticles(
    @Embedded val story: Story,
    @Relation(
        parentColumn = "id",
        entityColumn = "storyID"
    )
    val articles: List<Article>
)