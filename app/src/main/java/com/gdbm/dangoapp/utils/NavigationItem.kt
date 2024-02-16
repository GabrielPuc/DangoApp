package com.gdbm.dangoapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val label: String, val icons: ImageVector) {

    object Reference : NavigationItem("reference", "Reference", Icons.Default.Info)
    object Practice : NavigationItem("practice", "Practice", Icons.Default.Edit)

}