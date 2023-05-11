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
import org.threeten.bp.LocalDateTime

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val noteDao : NoteDao

    init {
        noteDao = TodoAppDatabase.getDatabase(application).getNoteDao()
    }

    private val _shouldCloseScreen = MutableLiveData<Int>()
    val shouldCloseScreen: LiveData<Int>
        get() = _shouldCloseScreen

    private var _currentNote = MutableLiveData<Note>()
    val currentNote: LiveData<Note>
        get() = _currentNote

    private var _pinned = MutableLiveData(false)
    val pinned: LiveData<Boolean>
        get() = _pinned

    private var repository = NoteRepositoryImpl(noteDao)

    fun finishWork(mode: Int) {
        _shouldCloseScreen.value = mode
    }

    fun getNote(noteId: Long) = viewModelScope.launch {
        val note = repository.getNote(noteId)
        _currentNote.value = note
        _pinned.value = note.pinned
    }

    fun toggleNotePinnedStatus() {
        _pinned.value ?: throw RuntimeException(
            "Cannot toggle note pinned status.Status is undefined."
        )
        _pinned.value = !_pinned.value!!
    }

    fun saveNote(inputTitle: String?, inputBody: String?) {
        val title = parseInput(inputTitle)
        val body = parseInput(inputBody)
        if (_currentNote.value == null) {
            if (isValidNote(title, body)) createNote(title, body)
        } else {
            if (isValidNote(title, body)) {
                editNote(title, body)
            }
            else {
                deleteNote()
                finishWork(EXIT_WITHOUT_SAVE_CHANGES)
            }
        }
    }

    fun manualDeleteNote() = viewModelScope.launch {
        currentNote.value?.id?.let { repository.deleteNote(it) }
        finishWork(EXIT_WITHOUT_SAVE_CHANGES)
    }

    private fun createNote(title: String, body: String) = viewModelScope.launch {
        _pinned.value ?: throw RuntimeException("Cannot create note. Pinned status is undefined.")
        val insertedId = repository.addNote(
            Note(title, body, _pinned.value!!, LocalDateTime.now())
        )

        getNote(insertedId)
    }

    private fun editNote(title: String, body: String) = viewModelScope.launch {
        currentNote.value ?: throw RuntimeException("Cannot edit note. Current note is empty.")
        _pinned.value ?: throw RuntimeException("Cannot edit note. Pinned status is undefined.")
        repository.editNote(
            currentNote.value!!.copy(
                title = title,
                body = body,
                pinned = _pinned.value!!,
                date = LocalDateTime.now()
            )
        )
    }

    private fun deleteNote() = viewModelScope.launch {
        currentNote.value ?: throw RuntimeException("Cannot delete note. Current note is empty.")
        currentNote.value!!.id?.let { repository.deleteNote(it) }
    }

    private fun parseInput(string: String?): String {
        return if (string.isNullOrBlank()) ""
        else string.trim()
    }

    private fun isValidNote(title: String, body: String): Boolean {
        return !(title.isEmpty() && body.isEmpty())
    }

    companion object {
        const val EXIT_SAVE_CHANGES = 100
        const val EXIT_WITHOUT_SAVE_CHANGES = 101
    }
}