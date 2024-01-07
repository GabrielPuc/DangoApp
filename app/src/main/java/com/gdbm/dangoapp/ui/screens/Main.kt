package com.gdbm.dangoapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.components.items.MenuItem
import com.gdbm.dangoapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main(mainViewModel: MainViewModel, screenTitle:String) {
    Scaffold(
        topBar = { NavBar(title = screenTitle) }
    ) { innerPadding ->
        val itsLoading by mainViewModel.itsLoading.collectAsState()

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            if (itsLoading) {
                CircularProgressIndicator()
            }else{
                LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)) {
                    Configs.MENU_OPTIONS.forEach { menuOption ->
                        item {
                            MenuItem(title = menuOption.name, subtitle = menuOption.subtitle) {
                                mainViewModel.actionSelected.value = menuOption.name
                            }
                        }
                    }
                }
            }
        }
    }
}