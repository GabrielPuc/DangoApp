package com.gdbm.dangoapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.viewmodel.SettingsViewModel
import java.util.Timer
import kotlin.concurrent.schedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDetail(settingsViewModel: SettingsViewModel) {
    val itsLoading by settingsViewModel.itsLoading.collectAsState()
    var switchState by remember { mutableStateOf(true) }
    Scaffold(
        topBar = { NavBar(title = "Settings", actions = {}) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(1f),
            contentAlignment = Alignment.Center
        ) {
            Column {
                if(itsLoading){
                    LinearProgressIndicator(
                        color = CustomColorsPalette.current.secondary,
                        strokeCap = StrokeCap.Round,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(24.dp)
                            .padding(10.dp)
                    )
                }

                androidx.compose.material.Button(
                    onClick = {
                        settingsViewModel.select("UPDATE")
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(1f),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = CustomColorsPalette.current.primary,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Check for content updates",
                        fontSize = 22.sp,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        color = Color.White
                    )
                }
            }

            /* STARTED IMPLEMENTATION OF DARK MODE TOGGLE
            Switch(
                checked = switchState,
                onCheckedChange ={
                    switchState=it
                    settingsViewModel.toggleDarkMode()
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = CustomColorsPalette.current.primary,
                    uncheckedThumbColor = CustomColorsPalette.current.primary,
                    checkedTrackColor = CustomColorsPalette.current.secondary,
                    uncheckedTrackColor = CustomColorsPalette.current.secondary,
                )
            )*/
        }
    }

}