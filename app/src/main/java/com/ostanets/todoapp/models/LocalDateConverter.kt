package com.ostanets.todoapp.models

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromString(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun toString(date: LocalDate?): String? {
        return date?.toString()
    }
}