package com.ostanets.todoapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import com.ostanets.todoapp.databinding.NoteItemBinding
import com.ostanets.todoapp.models.Note

class NotesListAdapter : ListAdapter<Note, NoteViewHolder>(NoteDiffCallback()), Filterable {
    var onNoteLongClickListener: ((Note) -> Unit)? = null
    var onNoteClickListener: ((Note) -> Unit)? = null

    private var notesList = mutableListOf<Note>()

    fun submitNoteList(notes: List<Note> ) {
        notesList.clear()
        notesList.addAll(notes)
        submitList(notesList)
    }

    override fun getItemViewType(position: Int): Int {
        return DEFAULT_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.setBody(note.body)
        holder.setDate(note.date)

        holder.view.setOnLongClickListener {
            onNoteLongClickListener?.invoke(note)
            true
        }

        holder.view.setOnClickListener {
            onNoteClickListener?.invoke(note)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint?.toString() ?: ""
                val filteredList = mutableListOf<Note>()
                notesList.forEach {
                    if (it.title.contains(query, ignoreCase = true) || it.body.contains(query, ignoreCase = true)) {
                        filteredList.add(it)
                    }
                }
                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val filteredList = results?.values as? List<Note> ?: return
                submitList(filteredList)
            }
        }
    }

    companion object {
        const val DEFAULT_TYPE = 1
        const val MAX_POOL_SIZE = 10
    }
}
