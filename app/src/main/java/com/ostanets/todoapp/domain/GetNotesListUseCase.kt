package com.ostanets.todoapp.domain

import androidx.lifecycle.LiveData

class GetNotesListUseCase(private val noteRepository: NoteRepository) {

    suspend fun getNotesList(): LiveData<List<Note>> {
        return noteRepository.getNotesList()
    }
}