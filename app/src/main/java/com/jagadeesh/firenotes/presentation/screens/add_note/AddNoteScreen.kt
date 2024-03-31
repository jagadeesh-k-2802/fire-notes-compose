package com.jagadeesh.firenotes.presentation.screens.add_note

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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import com.jagadeesh.firenotes.firebase.AuthRepository
import com.jagadeesh.firenotes.firebase.FirestoreRepository
import com.jagadeesh.firenotes.model.NoteModel
import com.jagadeesh.firenotes.presentation.components.AppBar
import com.jagadeesh.firenotes.presentation.main.MainViewModel
import com.jagadeesh.firenotes.presentation.navigation.Screens
import com.jagadeesh.firenotes.presentation.theme.layoutPadding
import com.meetup.twain.MarkdownEditor
import kotlinx.coroutines.launch

@Composable
fun AddNoteScreen(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val userUid by lazy { AuthRepository.getCurrentUser()?.uid!! }
    var title by rememberSaveable { mutableStateOf("") }

    var note by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val onCreate: () -> Unit = {
        if (title.isEmpty() || note.text.isEmpty()) {
            scope.launch { snackbarHostState.showSnackbar("Enter title and note") }
        } else {
            scope.launch {
                try {
                    val noteModel = NoteModel(title = title, note = note.text, ownerUid = userUid)
                    FirestoreRepository.addNote(noteModel)
                    viewModel.addNote(noteModel)
                    navController.popBackStack()
                } catch (exception: Exception) {
                    scope.launch { snackbarHostState.showSnackbar("Cannot add note") }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = Screens.AddNoteScreen.title,
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "Create Note")
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
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
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
        }
    }
}
