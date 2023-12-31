package com.gdbm.dangoapp.ui.screens

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gdbm.dangoapp.R
import com.gdbm.dangoapp.managers.drawing.DrawingManager
import com.gdbm.dangoapp.ui.theme.GreenBackground
import com.gdbm.dangoapp.ui.theme.GreenContrast
import com.gdbm.dangoapp.ui.theme.PinkBackground

@Composable
fun DrawingControlsBar(
    drawController: DrawingManager,
    undoVisibility: MutableState<Boolean>,
    redoVisibility: MutableState<Boolean>,
    orientation: Int
) {

    Box(
        modifier = Modifier
            .background(
                color = PinkBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .wrapContentWidth()
    ) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomIconButton(
                        R.drawable.ic_undo,
                        "undo",
                        if (undoVisibility.value) GreenContrast else Color.White,
                        text = "Undo"
                    ) {
                        if (undoVisibility.value) drawController.unDo()
                    }
                    CustomIconButton(
                        R.drawable.ic_redo,
                        "redo",
                        if (redoVisibility.value) GreenContrast else Color.White,
                        text = "Redo"
                    ) {
                        if (redoVisibility.value) drawController.reDo()
                    }
                    CustomIconButton(
                        R.drawable.ic_clear,
                        "reset",
                        if (redoVisibility.value || undoVisibility.value) GreenContrast else Color.White,
                        text = "Reset"
                    ) {
                        drawController.reset()
                    }
                }
            }

            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CustomIconButton(
                        R.drawable.ic_undo,
                        "undo",
                        if (undoVisibility.value) GreenContrast else Color.White,
                        text = "Undo"
                    ) {
                        if (undoVisibility.value) drawController.unDo()
                    }
                    CustomIconButton(
                        R.drawable.ic_redo,
                        "redo",
                        if (redoVisibility.value) GreenContrast else Color.White,
                        text = "Redo"
                    ) {
                        if (redoVisibility.value) drawController.reDo()
                    }
                    CustomIconButton(
                        R.drawable.ic_clear,
                        "reset",
                        if (redoVisibility.value || undoVisibility.value) GreenContrast else Color.White,
                        text = "Reset"
                    ) {
                        drawController.reset()
                    }
                }
            }
        }
    }

}

@Composable
fun SymbolsControlBar(
    drawController: DrawingManager,
    nextPressedCallback: () -> Unit,
    flipCardCallback: () -> Unit,
    isExperimentalEnabled: Boolean,
    orientation: Int
) {

    Box(
        modifier = Modifier
            .background(
                color = GreenBackground,
                shape = RoundedCornerShape(20.dp)
            )
            .wrapContentWidth()
    ) {
        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (isExperimentalEnabled) {
                        CustomIconButton(
                            R.drawable.ic_done,
                            "next",
                            Color.White,
                            text = "Verify"
                        ) {
                            drawController.saveBitmap()
                        }
                    }
                    CustomIconButton(
                        R.drawable.ic_card,
                        "next",
                        Color.White,
                        text = "Show"
                    ) {
                        flipCardCallback.invoke()
                    }
                    CustomIconButton(
                        R.drawable.ic_next,
                        "next",
                        Color.White,
                        text = "Skip"
                    ) {
                        nextPressedCallback.invoke()
                    }
                }
            }
            else -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (isExperimentalEnabled) {
                        CustomIconButton(
                            R.drawable.ic_done,
                            "next",
                            Color.White,
                            text = "Verify"
                        ) {
                            drawController.saveBitmap()
                        }
                    }
                    CustomIconButton(
                        R.drawable.ic_card,
                        "next",
                        Color.White,
                        text = "Show"
                    ) {
                        flipCardCallback.invoke()
                    }
                    CustomIconButton(
                        R.drawable.ic_next,
                        "next",
                        Color.White,
                        text = "Skip"
                    ) {
                        nextPressedCallback.invoke()
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomIconButton(
    @DrawableRes resId: Int,
    desc: String,
    colorTint: Color,
    border: Boolean = false,
    text: String,
    onClick: () -> Unit
) {
    val modifier = Modifier.size(24.dp)
    IconButton(
        onClick = onClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier) {
            Icon(
                painterResource(id = resId),
                contentDescription = desc,
                tint = colorTint,
                modifier = if (border) modifier.border(
                    0.5.dp,
                    Color.White,
                    shape = CircleShape
                ) else modifier
            )
            Text(text = text, color = colorTint)
        }

    }
}