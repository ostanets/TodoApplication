package com.ostanets.todoapp.domain

import androidx.lifecycle.LiveData

interface NoteRepository {

    suspend fun getNotesList(): LiveData<List<Note>>

    suspend fun getNote(noteId: Int): Note

    suspend fun addNote(note: Note)

    suspend fun editNote(note: Note)

    suspend fun deleteNote(noteId: Int)
}