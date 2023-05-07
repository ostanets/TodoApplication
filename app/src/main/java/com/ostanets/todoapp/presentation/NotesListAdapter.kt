package com.ostanets.todoapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.ostanets.todoapp.databinding.NoteItemBinding
import com.ostanets.todoapp.models.Note

class NotesListAdapter : ListAdapter<Note, NoteViewHolder>(NoteDiffCallback()) {
    var onNoteLongClickListener: ((Note) -> Unit)? = null
    var onNoteClickListener: ((Note) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return DEFAULT_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)

        if (note.body.isEmpty()) holder.setBody(note.title)
        else holder.setBody(note.body)

        holder.setDate(note.date)
        holder.setPinned(note.pinned)

        holder.view.setOnLongClickListener {
            onNoteLongClickListener?.invoke(note)
            true
        }

        holder.view.setOnClickListener {
            onNoteClickListener?.invoke(note)
        }
    }

    companion object {
        const val DEFAULT_TYPE = 1
        const val MAX_POOL_SIZE = 10
    }
}
