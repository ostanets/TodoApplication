package com.ostanets.todoapp.domain

import org.threeten.bp.LocalDateTime

data class Note(
    val title: String,
    val body: String,
    val pinned: Boolean,
    val date: LocalDateTime,
    val id: Long? = null
) {
    companion object {
        const val UNDEFINED_ID = -1L
    }
}
