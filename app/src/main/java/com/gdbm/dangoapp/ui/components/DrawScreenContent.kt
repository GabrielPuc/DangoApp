package com.gdbm.dangoapp.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.utils.extensions.vertical

@Composable
fun DrawScreenContent(
    currentOrientation: Int,
    innerPadding: PaddingValues,
    card: @Composable () -> Unit = {},
    brushSize: @Composable () -> Unit = {},
    drawingPad: @Composable () -> Unit = {},
    drawingControlBar: @Composable () -> Unit = {}
) {
    when (currentOrientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(1f)
                    .background(CustomColorsPalette.current.background),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                card()
                drawingPad()
                if(isTablet()){
                    Row(modifier = Modifier.vertical().rotate(-90f)) {
                        brushSize()
                    }
                    Row {
                        Column(modifier = Modifier.fillMaxHeight().padding(10.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            drawingControlBar()
                        }
                    }
                } else {
                    Row(modifier = Modifier.vertical().rotate(-90f)) {
                        brushSize()
                    }
                    Row(modifier = Modifier
                        .fillMaxHeight(1f)
                        .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                        drawingControlBar()
                    }
                }
            }
        }

        else -> {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(CustomColorsPalette.current.background)
                    .fillMaxHeight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                    card()
                }
                Row(modifier = Modifier.fillMaxWidth(1f)) {
                    drawingPad()
                }
                Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.Center) {
                    brushSize()
                }
                Row(modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(start = 10.dp, end = 10.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    drawingControlBar()
                }
            }
        }
    }
}

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        configuration.screenWidthDp > 840
    } else {
        configuration.screenWidthDp > 600
    }
}