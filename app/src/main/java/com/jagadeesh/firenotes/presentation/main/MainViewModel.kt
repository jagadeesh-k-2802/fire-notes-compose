package com.jagadeesh.firenotes.presentation.main

import androidx.lifecycle.ViewModel
import com.jagadeesh.firenotes.model.NoteModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * ViewModel to hold all notes for entire lifetime of the application
 */
class MainViewModel : ViewModel() {
    private val _notes: MutableStateFlow<List<NoteModel>?> = MutableStateFlow(null)
    val notes: StateFlow<List<NoteModel>?> = _notes

    fun setNotes(notes: List<NoteModel>) {
        _notes.value = notes.toList()
    }

    fun addNote(note: NoteModel) {
        val newNotes = mutableListOf(note)
        newNotes.addAll(_notes.value!!)
        _notes.value = newNotes.toList()
    }

    fun updateNote(note: NoteModel) {
        _notes.value = _notes.value?.map { if (it.id == note.id) note else it }
    }

    fun deleteNote(note: NoteModel) {
        _notes.value = _notes.value?.filter { it.id != note.id }
    }
}
