package com.ostanets.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(tableName = "schedules")
data class Schedule(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "place")
    val place: String,

    @ColumnInfo(name = "note")
    val note: String,

    @ColumnInfo(name = "full_day")
    val fullDay: Boolean,

    @ColumnInfo(name = "start")
    val start: LocalDateTime,

    @ColumnInfo(name = "finish")
    val finish: LocalDateTime,

    @ColumnInfo(name = "remind")
    val remind: TimeOffset,

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
