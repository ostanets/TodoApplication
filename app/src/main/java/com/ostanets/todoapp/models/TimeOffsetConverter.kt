package com.ostanets.todoapp.models

import androidx.room.TypeConverter
import java.lang.Exception

class TimeOffsetConverter {

    @TypeConverter
    fun fromInt(value: Int?): TimeOffset? {
        return value?.let {
            when (it) {
                5 -> TimeOffset.FIVE_MINUTES
                15 -> TimeOffset.FIFTEEN_MINUTES
                30 -> TimeOffset.THIRTY_MINUTES
                60 -> TimeOffset.ONE_HOUR
                1440 -> TimeOffset.ONE_DAY
                else -> throw Exception("Unresolved time offset: $value")
            }
        }
    }

    @TypeConverter
    fun toInt(timeOffset: TimeOffset?): Int? {
        return timeOffset?.minuets
    }
}