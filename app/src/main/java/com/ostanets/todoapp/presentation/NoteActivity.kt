package com.ostanets.todoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.ostanets.todoapp.databinding.ActivityNoteBinding

class NoteActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: ActivityNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.shouldCloseScreen.observe(this) {
            finish()
        }

        binding.btnBack.setOnClickListener {
            viewModel.finishWork()
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

        fun newIntentEditNote(context: Context, noteId: Int): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_NOTE_ID, noteId)
            return intent
        }
    }
}