package com.jagadeesh.firenotes.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    actions: (@Composable RowScope.() -> Unit)? = null
) {
    val navigationIcon: (@Composable () -> Unit) = {
        IconButton(onClick = onBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go Back"
            )
        }
    }

    if (showBack) {
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = navigationIcon,
            actions = { if (actions != null) actions(this) }
        )
    } else {
        TopAppBar(
            title = { Text(text = title) },
            actions = { if (actions != null) actions(this) }
        )
    }
}
