package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.domain.DeleteNoteUseCase
import com.ostanets.todoapp.domain.EditNoteUseCase
import com.ostanets.todoapp.domain.GetNotesListUseCase
import com.ostanets.todoapp.domain.NoteRepository
import com.ostanets.todoapp.models.Note
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepository

    init {
        repository = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private var getNotesListUseCase = GetNotesListUseCase(repository)
    private var editNoteUseCase = EditNoteUseCase(repository)
    private var deleteNoteUseCase = DeleteNoteUseCase(repository)

    private var _notesList = getNotesListUseCase.getNotesList()

    val notesList: LiveData<List<Note>>
        get() {
            return _notesList
        }
    private var _activeFragment: MutableLiveData<Int> = MutableLiveData(NOTES_FRAGMENT)
    val activeFragment: LiveData<Int>
        get() {
            return _activeFragment
        }

    fun togglePinStatus(note: Note) = viewModelScope.launch {
        editNoteUseCase.editNote(note.copy(pinned = !note.pinned))
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        note.id ?: throw RuntimeException("Cannot delete note. Note id is empty.")
        deleteNoteUseCase.deleteNote(note.id)
    }

    fun setActiveFragment(id: Int) {
        when (id) {
            NOTES_FRAGMENT -> _activeFragment.value = NOTES_FRAGMENT
            SCHEDULES_FRAGMENT -> _activeFragment.value = SCHEDULES_FRAGMENT
            else -> throw RuntimeException("Unknown fragment id $id")
        }
    }

    companion object {
        const val NOTES_FRAGMENT = 100
        const val SCHEDULES_FRAGMENT = 101
    }
}
