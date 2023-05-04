package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.domain.AddNoteUseCase
import com.ostanets.todoapp.domain.GetNotesListUseCase
import com.ostanets.todoapp.models.Note
import com.ostanets.todoapp.domain.NoteRepository
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepository

    init {
        repository = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private var addNoteUseCase = AddNoteUseCase(repository)
    private var getNotesListUseCase = GetNotesListUseCase(repository)


    var notesList: LiveData<List<Note>> = getNotesListUseCase.getNotesList()

    fun createNote(title: String, body: String) = viewModelScope.launch {
        addNoteUseCase.addNote(Note(title, body,false, LocalDate.now()))
    }
}
