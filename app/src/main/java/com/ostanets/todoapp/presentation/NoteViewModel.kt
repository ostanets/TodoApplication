package com.ostanets.todoapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.domain.AddNoteUseCase
import com.ostanets.todoapp.domain.DeleteNoteUseCase
import com.ostanets.todoapp.domain.EditNoteUseCase
import com.ostanets.todoapp.domain.GetNoteUseCase
import com.ostanets.todoapp.domain.NoteRepository
import com.ostanets.todoapp.models.Note
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    init {
        repository = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private val _shouldCloseScreen = MutableLiveData<Unit>()

    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private var _currentNote = MutableLiveData<Note>()
    val currentNote: LiveData<Note>
        get() = _currentNote

    private var getNoteUseCase = GetNoteUseCase(repository)
    private var addNoteUseCase = AddNoteUseCase(repository)
    private var editNoteUseCase = EditNoteUseCase(repository)
    private var deleteNoteUseCase = DeleteNoteUseCase(repository)

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

    fun getNote(noteId: Long)  = viewModelScope.launch {
        _currentNote.value = getNoteUseCase.getNote(noteId)
    }
    fun saveNote(inputTitle: String?, inputBody: String?) {
        val title = parseInput(inputTitle)
        val body = parseInput(inputBody)
        if (_currentNote.value == null) {
            if (isValidNote(title, body)) createNote(title, body)
        } else {
            if (isValidNote(title, body)) editNote(title, body)
            else deleteNote()
        }
    }

    private fun createNote(title: String, body: String) = viewModelScope.launch {
        addNoteUseCase.addNote(Note(title, body, false, LocalDate.now()))
    }

    private fun editNote(title: String, body: String) = viewModelScope.launch {
        currentNote.value ?: throw RuntimeException("Cannot edit note. Current note is empty.")
        editNoteUseCase.editNote(
            currentNote.value!!.copy(
                title = title,
                body = body,
                date = LocalDate.now()
            )
        )
    }

    private fun deleteNote() = viewModelScope.launch {
        currentNote.value ?: throw RuntimeException("Cannot delete note. Current note is empty.")
        currentNote.value!!.id?.let { deleteNoteUseCase.deleteNote(it) }
    }

    private fun parseInput(string: String?): String {
        return if (string.isNullOrBlank()) ""
        else string.trim()
    }

    private fun isValidNote(title: String, body: String): Boolean {
        return !(title.isEmpty() && body.isEmpty())
    }
}