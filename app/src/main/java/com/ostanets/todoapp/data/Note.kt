package com.ostanets.todoapp.data

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
)

fun com.ostanets.todoapp.domain.Note.toEntity(): Note {
    return Note(title, body, pinned, date, id)
}

fun Note.fromEntity(): com.ostanets.todoapp.domain.Note {
    return com.ostanets.todoapp.domain.Note(title, body, pinned, date, id)
}
