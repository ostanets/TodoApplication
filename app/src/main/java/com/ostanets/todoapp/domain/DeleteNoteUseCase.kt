package com.ostanets.todoapp.domain

class DeleteNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun deleteNote(noteId: Int) {
        noteRepository.deleteNote(noteId)
    }
}