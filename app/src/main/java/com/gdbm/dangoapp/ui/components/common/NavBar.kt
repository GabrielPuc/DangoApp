package com.gdbm.dangoapp.ui.components.common

import androidx.compose.material.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette

@Composable
fun NavBar(title:String, actions:@Composable () -> Unit = {}) {
    TopAppBar(
        backgroundColor = CustomColorsPalette.current.primary,
        title = {
            Text(text = title, color = CustomColorsPalette.current.textColor)
        },
        actions = {
            actions.invoke()
        }
    )
}