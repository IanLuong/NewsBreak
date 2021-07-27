package com.ianluong.newsbreak.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ianluong.newsbreak.api.Article
import java.util.*

class StoryTypeConverters {

    var gson: Gson = Gson()

    @TypeConverter
    fun toUUID(id: String?): UUID? {
        return UUID.fromString(id)
    }

    @TypeConverter
    fun fromUUID(id: UUID?): String? {
        return id?.toString()
    }


}