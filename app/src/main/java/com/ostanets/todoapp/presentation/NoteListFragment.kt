package com.ostanets.todoapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ostanets.todoapp.databinding.FragmentNoteListBinding
import com.ostanets.todoapp.presentation.NotesListAdapter.Companion.DEFAULT_TYPE

class NoteListFragment : Fragment() {
    private lateinit var binding: FragmentNoteListBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var notesListAdapter: NotesListAdapter

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

        setupRecycleView()

        binding.svNoteList.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesListAdapter.filter.filter(newText)
                return true
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notesList.observe(viewLifecycleOwner) {
            notesListAdapter.submitNoteList(it)
        }
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = NoteListFragment().apply {}
    }
}