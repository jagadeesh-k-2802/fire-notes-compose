package com.jagadeesh.firenotes.presentation.screens.home

import com.jagadeesh.firenotes.model.NoteModel

internal sealed class HomeScreenState(
    val notes: List<NoteModel>? = null,
    val error: String? = null
) {
    class Success(notes: List<NoteModel>) : HomeScreenState(notes = notes)
    class Error(error: String) : HomeScreenState(error = error)
    data object Loading : HomeScreenState()
}
