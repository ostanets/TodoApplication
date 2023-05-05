package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.domain.AddNoteUseCase
import com.ostanets.todoapp.domain.GetNotesListUseCase
import com.ostanets.todoapp.models.Note
import com.ostanets.todoapp.domain.NoteRepository
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.lang.RuntimeException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepository

    init {
        repository = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private var addNoteUseCase = AddNoteUseCase(repository)
    private var getNotesListUseCase = GetNotesListUseCase(repository)


    var notesList: LiveData<List<Note>> = getNotesListUseCase.getNotesList()
    private var _activeFragment: MutableLiveData<Int> = MutableLiveData(NOTES_FRAGMENT)
    val activeFragment: LiveData<Int>
        get() {
            return _activeFragment
        }

    fun createNote(title: String, body: String) = viewModelScope.launch {
        addNoteUseCase.addNote(Note(title, body,false, LocalDate.now()))
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
