package com.jagadeesh.firenotes.presentation.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jagadeesh.firenotes.firebase.AuthRepository
import com.jagadeesh.firenotes.presentation.components.AppBar
import com.jagadeesh.firenotes.presentation.navigation.Screens
import com.jagadeesh.firenotes.presentation.theme.layoutPadding

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AppBar(
                title = Screens.SettingsScreen.title,
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
        ) {
            ListItem(
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                },
                headlineContent = { Text("Currently logged in as") },
                supportingContent = { Text(text = AuthRepository.getCurrentUser()?.email!!) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { AuthRepository.signOut() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = layoutPadding)
            ) {
                Text(text = "LOGOUT")
            }
        }
    }
}
