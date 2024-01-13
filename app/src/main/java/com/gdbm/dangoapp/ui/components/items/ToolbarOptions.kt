package com.gdbm.dangoapp.ui.components.items

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun SettingsButton(action:() -> Unit) {
    IconButton(onClick = {
        action()
    }) {
        Icon(Icons.Filled.Settings, "settings icon")
    }
}