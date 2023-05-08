package com.ostanets.todoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ostanets.todoapp.databinding.FragmentNoteListBinding
import com.ostanets.todoapp.models.Note
import com.ostanets.todoapp.presentation.NotesListAdapter.Companion.DEFAULT_TYPE

class NoteListFragment : Fragment() {
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var notesListAdapter: NotesListAdapter
    private var notesList = ArrayList<Note>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListBinding.inflate(layoutInflater)

        val factory = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(requireActivity().application)
        viewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[MainViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()

        binding.svNoteList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredNotesList = filterNotesList(newText)
                notesListAdapter.submitList(sortNotesList(filteredNotesList))
                return true
            }
        })
    }

    override fun onResume() {
        viewModel.notesList.observe(viewLifecycleOwner) { notes ->
            notesList = notes as ArrayList<Note>
            notesListAdapter.submitList(sortNotesList(notes))
        }
        super.onResume()
    }

    private fun sortNotesList(note: List<Note>) =
        note.sortedWith(compareByDescending<Note> { it.pinned }
            .thenByDescending { it.date }
        )

    private fun filterNotesList(text: String?): List<Note> {
        val filteredList = ArrayList<Note>()

        if (text.isNullOrBlank()) return notesList

        for (item in notesList) {
            if (item.title.lowercase().contains(text.lowercase()) ||
                item.body.lowercase().contains(text.lowercase())) {
                filteredList.add(item)
            }
        }

        return filteredList
    }


    private fun setupRecycleView() {
        val rvNoteList = binding.rvNoteList
        notesListAdapter = NotesListAdapter()

        with(rvNoteList) {
            layoutManager = LinearLayoutManager(binding.root.context)
            adapter = notesListAdapter
            recycledViewPool.setMaxRecycledViews(
                DEFAULT_TYPE,
                NotesListAdapter.MAX_POOL_SIZE
            )
        }

        setupNoteClickListener()
        setupNoteLongClickListener()
        setupNoteSwipeListener(rvNoteList)
    }

    private fun setupNoteSwipeListener(rvNotesList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteNote(notesListAdapter.currentList[viewHolder.adapterPosition])
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvNotesList)
    }

    private fun setupNoteLongClickListener() {
        notesListAdapter.onNoteLongClickListener = {
            viewModel.togglePinStatus(it)
        }
    }

    private fun setupNoteClickListener() {
        notesListAdapter.onNoteClickListener = {
            val intent = if (it.id != null) {
                NoteActivity.newIntentEditNote(binding.root.context, it.id)
            } else {
                NoteActivity.newIntentAddNote(binding.root.context)
            }
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteListFragment().apply {}
    }
}