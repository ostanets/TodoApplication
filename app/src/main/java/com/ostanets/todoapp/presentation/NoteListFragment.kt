package com.ostanets.todoapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notesList.observe(viewLifecycleOwner) {
            notesListAdapter.submitList(it)
            Log.d("NoteListFragment", "onViewCreated: $it")
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