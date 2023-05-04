package com.ostanets.todoapp.domain

import com.ostanets.todoapp.models.Note

class GetNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun getNote(noteId: Long): Note {
        return noteRepository.getNote(noteId)
    }
}