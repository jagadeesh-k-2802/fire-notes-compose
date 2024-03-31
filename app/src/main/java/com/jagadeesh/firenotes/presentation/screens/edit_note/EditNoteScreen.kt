package com.jagadeesh.firenotes.presentation.screens.edit_note

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jagadeesh.firenotes.R
import com.jagadeesh.firenotes.firebase.FirestoreRepository
import com.jagadeesh.firenotes.model.NoteModel
import com.jagadeesh.firenotes.presentation.components.AppBar
import com.jagadeesh.firenotes.presentation.main.MainViewModel
import com.jagadeesh.firenotes.presentation.navigation.Screens
import com.jagadeesh.firenotes.presentation.theme.layoutPadding
import com.meetup.twain.MarkdownEditor
import kotlinx.coroutines.launch

@Composable
fun EditNoteScreen(navController: NavController, viewModel: MainViewModel) {
    val noteModel = NoteModel.deSerialize(
        navController.currentBackStackEntry?.arguments?.getString("note") ?: ".,.,.,."
    )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var title by rememberSaveable { mutableStateOf(noteModel.title) }

    var note by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(noteModel.note))
    }

    val onUpdate: () -> Unit = {
        scope.launch {
            try {
                val newNoteModel = noteModel.update(title = title, note = note.text)
                FirestoreRepository.updateNote(newNoteModel)
                viewModel.updateNote(newNoteModel)
                navController.popBackStack()
            } catch (exception: Exception) {
                scope.launch { snackbarHostState.showSnackbar("Cannot update note") }
            }
        }
    }

    val onDelete: () -> Unit = {
        scope.launch {
            try {
                openDialog = false
                FirestoreRepository.deleteNote(noteModel)
                viewModel.deleteNote(noteModel)
                navController.popBackStack()
            } catch (exception: Exception) {
                scope.launch { snackbarHostState.showSnackbar("Cannot delete note") }
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = Screens.EditNoteScreen.title,
                showBack = true,
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { openDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onUpdate) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Update Note")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(contentPadding)
                .padding(horizontal = layoutPadding)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Title") },
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clip(RectangleShape)
                    .border(width = 2.dp, color = Color.LightGray)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                MarkdownEditor(
                    value = note,
                    onValueChange = { value -> note = value.copy(value.text) },
                    modifier = Modifier.fillMaxWidth(),
                    hint = R.string.note_hint
                )
            }

            if (openDialog) AlertDialog(
                onDismissRequest = { openDialog = false },
                title = { Text(text = "Do you want to delete this note ?") },
                confirmButton = { TextButton(onClick = onDelete) { Text("YES") } },
                dismissButton = { TextButton(onClick = { openDialog = false }) { Text("CANCEL") } }
            )
        }
    }
}
