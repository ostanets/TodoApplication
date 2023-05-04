package com.ostanets.todoapp.domain

data class Note(
    val title: String,
    val body: String,
    val pinned: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
