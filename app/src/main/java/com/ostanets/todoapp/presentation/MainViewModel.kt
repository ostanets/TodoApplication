package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.domain.GetNotesListUseCase
import com.ostanets.todoapp.domain.NoteRepository
import com.ostanets.todoapp.models.Note

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : NoteRepository

    init {
        repository = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private var getNotesListUseCase = GetNotesListUseCase(repository)


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
