package com.ostanets.todoapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ostanets.todoapp.R
import com.ostanets.todoapp.databinding.ActivityNoteBinding
import com.ostanets.todoapp.models.Note

class NoteActivity : AppCompatActivity() {
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: ActivityNoteBinding

    private var exitMode = NoteViewModel.EXIT_SAVE_CHANGES
    private var screenMode = MODE_UNKNOWN
    private var noteId = Note.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseIntent()

        window.clearFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }

        initFinish()
        setupButtonMore()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val title = binding.etTitle.text.toString()
        val body = binding.etBody.text.toString()
        return when (item.itemId) {
            R.id.action_save -> {
                viewModel.saveNote(title, body)
                true
            }

            R.id.action_share -> {
                val message = if (title.isNotBlank() && body.isNotBlank()) {
                    "$title\n$body"
                } else {
                    title.ifBlank { body }
                }

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
                true
            }

            R.id.action_delete -> {
                if (title.isNotBlank() || body.isNotBlank()) {
                    val alertDialog = setupAlertDialog()
                    alertDialog.show()
                } else {
                    viewModel.manualDeleteNote()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAlertDialog() = MaterialAlertDialogBuilder(binding.root.context)
        .setTitle(resources.getString(R.string.confirm_deletion_title))
        .setMessage(resources.getString(R.string.confirm_deletion_description))
        .setPositiveButton(
            resources.getString(R.string.confirm_deletion_accept)
        ) { _, _ ->
            viewModel.manualDeleteNote()
        }
        .setNegativeButton(
            resources.getString(R.string.confirm_deletion_decline)
        ) {_, _ -> }
        .create()

    private fun setupButtonMore() {
        binding.buttonMore.setOnClickListener {
            showMenu(it)
        }
    }

    private fun showMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener { item ->
            onOptionsItemSelected(item)
        }
        popup.inflate(R.menu.note_menu)

        popup.setForceShowIcon(true)
        popup.show()
    }

    private fun initFinish() {
        viewModel.shouldCloseScreen.observe(this) {
            exitMode = it
            finish()
        }

        binding.btnBack.setOnClickListener {
            viewModel.finishWork(NoteViewModel.EXIT_SAVE_CHANGES)
        }
    }

    override fun onStop() {
        if (exitMode == NoteViewModel.EXIT_SAVE_CHANGES) {
            viewModel.saveNote(
                binding.etTitle.text.toString(),
                binding.etBody.text.toString()
            )
        }

        super.onStop()
    }

    private fun setupPinButton() {
        viewModel.pinned.observe(this) { status ->
            if (status) {
                binding.buttonPin.setImageResource(R.drawable.baseline_thumbtack_24)
            } else {
                binding.buttonPin.setImageResource(R.drawable.baseline_thumbtack_inactive_24)
            }
        }

        binding.buttonPin.setOnClickListener {
            viewModel.toggleNotePinnedStatus()
        }
    }

    private fun launchAddMode() {
        setupPinButton()
    }

    private fun launchEditMode() {
        viewModel.getNote(noteId)
        viewModel.currentNote.observe(this) {
            binding.title = it.title
            binding.body = it.body
        }

        setupPinButton()
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