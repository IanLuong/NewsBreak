package com.ianluong.newsbreak.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.ianluong.newsbreak.api.Article
import java.util.*

class StoryTypeConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }

    @TypeConverter
    fun toUUID(id: String?): UUID? {
        return UUID.fromString(id)
    }

    @TypeConverter
    fun fromUUID(id: UUID?): String? {
        return id?.toString()
    }
}