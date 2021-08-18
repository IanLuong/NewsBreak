package com.ianluong.newsbreak.database

import androidx.room.*
import com.ianluong.newsbreak.api.Article
import java.io.Serializable
import java.util.*

@Entity
class Story: Serializable {
    @PrimaryKey var storyId: UUID = UUID.randomUUID()
    var title: String? = null
}

@Entity(primaryKeys = ["storyId", "articleId"])
data class StoryArticleCrossRef(
    val storyId: UUID,
    val articleId: UUID
)

data class StoryWithArticles(
    @Embedded val story: Story,
    @Relation(
        parentColumn = "storyId",
        entityColumn = "articleId",
        associateBy = Junction(StoryArticleCrossRef::class)
    )
    val articles: List<Article>
)

data class ArticleWithStories(
    @Embedded val article: Article,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "storyId",
        associateBy = Junction(StoryArticleCrossRef::class)
    )
    val stories: List<Story>
)