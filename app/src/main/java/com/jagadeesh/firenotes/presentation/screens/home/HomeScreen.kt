package com.jagadeesh.firenotes.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.jagadeesh.firenotes.firebase.AuthRepository
import com.jagadeesh.firenotes.firebase.FirestoreRepository
import com.jagadeesh.firenotes.presentation.components.AppBar
import com.jagadeesh.firenotes.presentation.main.MainViewModel
import com.jagadeesh.firenotes.presentation.navigation.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    val scope = rememberCoroutineScope()

    var homeScreenState: HomeScreenState by remember {
        if (viewModel.notes.value != null) {
            mutableStateOf(HomeScreenState.Success(viewModel.notes.value!!))
        } else {
            mutableStateOf(HomeScreenState.Loading)
        }
    }

    val fetchData = {
        scope.launch(Dispatchers.IO) {
            homeScreenState = HomeScreenState.Loading

            homeScreenState = try {
                HomeScreenState.Success(
                    FirestoreRepository.getAllNotesFromUser(
                        AuthRepository.getCurrentUser()?.uid!!
                    )
                )
            } catch (exception: Exception) {
                HomeScreenState.Error(
                    exception.message ?: "Error fetching notes"
                )
            }

            if (homeScreenState is HomeScreenState.Success) {
                viewModel.setNotes(homeScreenState.notes!!)
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (homeScreenState !is HomeScreenState.Success) fetchData()
    }

    Scaffold(
        topBar = {
            AppBar(
                title = Screens.HomeScreen.title,
                actions = {
                    IconButton(onClick = { fetchData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                    IconButton(
                        onClick = { navController.navigate(Screens.SettingsScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "More"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screens.AddNoteScreen.route) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }
    ) { contentPadding ->
        when (homeScreenState) {
            is HomeScreenState.Success -> {
                if (homeScreenState.notes?.isEmpty() == true) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(contentPadding)
                    ) {
                        Text(
                            text = "No Notes",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    if (homeScreenState.notes != null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(contentPadding)
                        ) {
                            items(homeScreenState.notes?.size ?: 0) {
                                val note = homeScreenState.notes!![it]

                                ListItem(
                                    headlineContent = { Text(note.title) },
                                    modifier = Modifier.clickable {
                                        navController.navigate(
                                            Screens.EditNoteScreen.route + "/${homeScreenState.notes!![it].serialize()}"
                                        )
                                    }
                                )

                                // If not last item
                                if (it != homeScreenState.notes!!.size - 1) HorizontalDivider()
                            }
                        }
                    }
                }
            }

            is HomeScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }

            is HomeScreenState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = homeScreenState.error!!,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
