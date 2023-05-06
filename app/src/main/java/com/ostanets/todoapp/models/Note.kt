package com.ostanets.todoapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate


@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "body")
    val body: String,

    @ColumnInfo(name = "pinned")
    val pinned: Boolean,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
