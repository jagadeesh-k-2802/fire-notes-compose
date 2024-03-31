package com.jagadeesh.firenotes.presentation.navigation

sealed class Screens(val route: String, val title: String) {
    data object LoginScreen : Screens("login", "Login")
    data object RegisterScreen : Screens("register", "Register")

    data object HomeScreen : Screens("home", "Home")
    data object AddNoteScreen : Screens("add-note", "Add Note")
    data object EditNoteScreen : Screens("edit-note", "Edit Note")
    data object SettingsScreen : Screens("settings", "Settings")
}

