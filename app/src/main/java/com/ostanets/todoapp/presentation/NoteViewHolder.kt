package com.ostanets.todoapp.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ostanets.todoapp.databinding.NoteItemBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

class NoteViewHolder(private val binding: NoteItemBinding) : ViewHolder(binding.root) {
    val view: View = binding.root

    fun setBody(content: String) {
        binding.body = content
    }

    fun setDate(date: LocalDate) {
        binding.date = formatter.format(date)
    }
}
