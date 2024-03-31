package com.jagadeesh.firenotes.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jagadeesh.firenotes.presentation.screens.add_note.AddNoteScreen
import com.jagadeesh.firenotes.presentation.screens.edit_note.EditNoteScreen
import com.jagadeesh.firenotes.presentation.screens.home.HomeScreen
import com.jagadeesh.firenotes.presentation.screens.login.LoginScreen
import com.jagadeesh.firenotes.presentation.screens.register.RegisterScreen
import com.jagadeesh.firenotes.presentation.screens.settings.SettingsScreen
import com.jagadeesh.firenotes.firebase.AuthRepository
import com.jagadeesh.firenotes.presentation.navigation.Screens
import com.jagadeesh.firenotes.presentation.theme.FireNotesTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FireNotesTheme {
                val currentUser = AuthRepository.rememberCurrentUser()

                if (currentUser.value != null) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screens.HomeScreen.route
                    ) {
                        composable(Screens.HomeScreen.route) {
                            HomeScreen(
                                navController = navController,
                                viewModel = viewModel
                            )
                        }

                        composable(Screens.AddNoteScreen.route) {
                            AddNoteScreen(
                                navController,
                                viewModel = viewModel
                            )
                        }

                        composable(Screens.EditNoteScreen.route + "/{note}") {
                            EditNoteScreen(
                                navController,
                                viewModel = viewModel
                            )
                        }

                        composable(Screens.SettingsScreen.route) {
                            SettingsScreen(navController)
                        }
                    }
                } else {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screens.LoginScreen.route
                    ) {
                        composable(Screens.LoginScreen.route) {
                            LoginScreen(navController)
                        }

                        composable(Screens.RegisterScreen.route) {
                            RegisterScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
