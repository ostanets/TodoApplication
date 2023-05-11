package com.ostanets.todoapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.ostanets.todoapp.domain.Note
import com.ostanets.todoapp.domain.NoteRepository
import java.lang.RuntimeException

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {

    override fun getNotesList(): LiveData<List<Note>> {
        return noteDao.getNotesList().map { notes ->
            notes.map { it.fromEntity() }
        }
    }

    override suspend fun getNote(noteId: Long): Note {
        val note = noteDao.getNote(noteId)
        return note.fromEntity()
    }

    override suspend fun addNote(note: Note): Long {
        return noteDao.addNote(note.toEntity())
    }

    override suspend fun editNote(note: Note) {
        val id = note.id ?: throw RuntimeException("Undefined id")
        noteDao.editNote(id, note.title, note.body, note.pinned, note.date)
    }

    override suspend fun deleteNote(noteId: Long) {
        noteDao.deleteNote(noteId)
    }
}