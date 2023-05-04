package com.ostanets.todoapp.domain

class EditNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun editNote(note: Note) {
        noteRepository.editNote(note)
    }
}