package com.ostanets.todoapp.domain

class AddNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun addNote(note: Note) {
        noteRepository.addNote(note)
    }
}