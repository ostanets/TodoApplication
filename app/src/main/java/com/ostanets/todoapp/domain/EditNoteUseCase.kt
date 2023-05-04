package com.ostanets.todoapp.domain

import com.ostanets.todoapp.models.Note

class EditNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun editNote(note: Note) {
        note.id?.let { noteRepository.editNote(it, note.title, note.body, note.pinned, note.date) }
    }
}