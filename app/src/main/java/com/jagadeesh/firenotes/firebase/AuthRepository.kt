package com.jagadeesh.firenotes.firebase

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await

object AuthRepository {
    private val auth: FirebaseAuth = Firebase.auth
    private val currentUser = MutableStateFlow(auth.currentUser)
    fun getCurrentUser() = auth.currentUser

    init {
        auth.addAuthStateListener { currentUser.value = it.currentUser }
    }

    @Composable
    fun rememberCurrentUser(): State<FirebaseUser?> {
        return currentUser.collectAsState()
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (exception: Exception) {
            throw exception
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
