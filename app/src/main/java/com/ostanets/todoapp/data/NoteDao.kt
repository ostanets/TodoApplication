package com.ostanets.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDateTime

@Dao
interface NoteDao {

    @Query("SELECT * FROM `notes` ORDER BY id ASC")
    fun getNotesList(): LiveData<List<Note>>

    @Query("SELECT * FROM `notes` WHERE id = :noteId")
    suspend fun getNote(noteId: Long): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note): Long

    @Query("UPDATE `notes` SET title = :title, body = :body, pinned = :pinned, date = :date WHERE id = :id")
    suspend fun editNote(id: Long, title: String, body: String, pinned: Boolean, date: LocalDateTime)

    @Query("DELETE FROM `notes` WHERE id = :noteId")
    suspend fun deleteNote(noteId: Long)
}