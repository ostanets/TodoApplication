package com.ostanets.todoapp.domain

import com.ostanets.todoapp.models.Note


class AddNoteUseCase(private val noteRepository: NoteRepository) {

    suspend fun addNote(note: Note): Long {
        return noteRepository.addNote(note)
    }
}