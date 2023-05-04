package com.ostanets.todoapp.presentation

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ostanets.todoapp.R
import com.ostanets.todoapp.data.TodoAppDatabase
import com.ostanets.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoAppDatabase
    private lateinit var viewModel: MainViewModel

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = TodoAppDatabase.getDatabase(this)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MainViewModel::class.java]

        setupSwitcher()
        setupFABs()

    }

    private fun setupSwitcher() {
        binding.switchToNotes.setOnClickListener {
            openNotesFragment()
        }

        binding.switchToSchedule.setOnClickListener {
            openScheduleFragment()
        }
    }

    private fun openNotesFragment() {
        val activeColor = ContextCompat.getColor(binding.root.context, R.color.switcher_active)
        binding.switchToNotes.background.setTint(activeColor)

        val inactiveColor = ContextCompat.getColor(binding.root.context, R.color.switcher_inactive)
        binding.switchToSchedule.background.setTint(inactiveColor)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame, NoteListFragment.newInstance())
            .commit()
    }

    private fun openScheduleFragment() {
        val activeColor = ContextCompat.getColor(binding.root.context, R.color.switcher_active)
        binding.switchToSchedule.background.setTint(activeColor)

        val inactiveColor = ContextCompat.getColor(binding.root.context, R.color.switcher_inactive)
        binding.switchToNotes.background.setTint(inactiveColor)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frame, NoteListFragment.newInstance())
            .commit()

        Toast.makeText(this, "Расписание еще не создано", Toast.LENGTH_SHORT).show()
    }

    private fun setupFABs() {
        binding.mainFab.setOnClickListener {
            onAddButtonClicked()
        }

        binding.noteFab.setOnClickListener {
            val intent = NoteActivity.newIntentAddNote(this)
            startActivity(intent)
            onAddButtonClicked()
        }

        binding.scheduleFab.setOnClickListener {
            Toast.makeText(this, "Расписание еще не создано", Toast.LENGTH_SHORT).show()
            onAddButtonClicked()
        }
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.noteFab.startAnimation(fromBottom)
            binding.noteTextView.startAnimation(fromBottom)
            binding.scheduleFab.startAnimation(fromBottom)
            binding.scheduleTextView.startAnimation(fromBottom)
            binding.mainFab.startAnimation(rotateOpen)
        } else {
            binding.noteFab.startAnimation(toBottom)
            binding.noteTextView.startAnimation(toBottom)
            binding.scheduleFab.startAnimation(toBottom)
            binding.scheduleTextView.startAnimation(toBottom)
            binding.mainFab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.noteFab.visibility = View.VISIBLE
            binding.noteTextView.visibility = View.VISIBLE
            binding.scheduleFab.visibility = View.VISIBLE
            binding.scheduleTextView.visibility = View.VISIBLE
        } else {
            binding.noteFab.visibility = View.GONE
            binding.noteTextView.visibility = View.GONE
            binding.scheduleFab.visibility = View.GONE
            binding.scheduleTextView.visibility = View.GONE
        }
    }
}