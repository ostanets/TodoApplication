package com.ostanets.todoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ostanets.todoapp.databinding.ActivityNoteBinding
import com.ostanets.todoapp.models.Note

class NoteActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: ActivityNoteBinding

    private var screenMode = MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseIntent()
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }

        binding.btnBack.setOnClickListener {
            viewModel.finishWork()
        }
    }

    override fun onStop() {
        viewModel.saveNote(
            binding.etTitle.text.toString(),
            binding.etBody.text.toString()
        )
        super.onStop()
    }

    private fun launchAddMode() {

    }

    private fun launchEditMode() {
        viewModel.getNote(noteId)
        viewModel.currentNote.observe(this) {
            binding.title = it.title
            binding.body = it.body
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode

        if (mode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_NOTE_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, Note.UNDEFINED_ID)
            if (noteId < 0) {
                throw RuntimeException("Invalid shop item id $noteId")

            }
        }
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_NOTE_ID = "extra_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddNote(context: Context): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun newIntentEditNote(context: Context, noteId: Long): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_NOTE_ID, noteId)
            return intent
        }
    }
}