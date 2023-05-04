package com.ostanets.todoapp.domain

import androidx.lifecycle.LiveData
import com.ostanets.todoapp.models.Note

class GetNotesListUseCase(private val noteRepository: NoteRepository) {

    fun getNotesList(): LiveData<List<Note>> {
        return noteRepository.getNotesList()
    }
}