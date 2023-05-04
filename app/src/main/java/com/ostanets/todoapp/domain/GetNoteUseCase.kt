package com.ostanets.todoapp.domain

class GetNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun getNote(noteId: Int): Note {
        return noteRepository.getNote(noteId)
    }
}