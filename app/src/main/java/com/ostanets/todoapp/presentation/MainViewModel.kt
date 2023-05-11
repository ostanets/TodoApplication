package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.data.NoteDao
import com.ostanets.todoapp.data.NoteRepositoryImpl
import com.ostanets.todoapp.domain.Note
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val noteDao : NoteDao

    init {
        noteDao = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private var repository = NoteRepositoryImpl(noteDao)

    private var _notesList = repository.getNotesList()

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
        repository.editNote(note.copy(pinned = !note.pinned))
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        note.id ?: throw RuntimeException("Cannot delete note. Note id is empty.")
        repository.deleteNote(note.id)
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
