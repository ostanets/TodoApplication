package com.ostanets.todoapp.domain

import com.ostanets.todoapp.models.Note
import java.lang.RuntimeException

class EditNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun editNote(note: Note) {
        val id = note.id ?: throw RuntimeException("Undefined id")
        noteRepository.editNote(id, note.title, note.body, note.pinned, note.date)
    }
}