package com.jagadeesh.firenotes.presentation.screens.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jagadeesh.firenotes.firebase.AuthRepository
import com.jagadeesh.firenotes.presentation.components.AppBar
import com.jagadeesh.firenotes.presentation.navigation.Screens
import com.jagadeesh.firenotes.presentation.theme.layoutPadding
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val onRegister: () -> Unit = {
        if (email.isEmpty() || password.isEmpty()) {
            scope.launch { snackbarHostState.showSnackbar("Enter email and password") }
        } else {
            scope.launch {
                try {
                    AuthRepository.createUserWithEmailAndPassword(email, password)
                } catch (exception: Exception) {
                    scope.launch { snackbarHostState.showSnackbar("Register Error") }
                }
            }
        }
    }

    Scaffold(
        topBar = { AppBar(title = Screens.RegisterScreen.title) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(horizontal = layoutPadding)
        ) {
            Text(
                text = "FireNotes",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge
            )

            Spacer(modifier = Modifier.height(180.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Email") },
                placeholder = { Text(text = "Example@mail.com") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(3.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password") },
                placeholder = { Text(text = "Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff
                            else Icons.Default.Visibility,
                            contentDescription = "Show / Hide Password"
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = onRegister, modifier = Modifier.fillMaxWidth()) {
                Text(text = "REGISTER")
            }

            Text(
                text = "Already have an account? Login",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 30.dp)
                    .clickable { navController.popBackStack() }
            )
        }
    }
}
