package com.ostanets.todoapp.domain

import androidx.lifecycle.LiveData

interface NoteRepository {

    fun getNotesList(): LiveData<List<Note>>

    suspend fun getNote(noteId: Long): Note

    suspend fun addNote(note: Note): Long

    suspend fun editNote(note: Note)

    suspend fun deleteNote(noteId: Long)
}