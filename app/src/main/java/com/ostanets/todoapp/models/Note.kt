package com.ostanets.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime


@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "pinned")
    val pinned: Boolean,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
