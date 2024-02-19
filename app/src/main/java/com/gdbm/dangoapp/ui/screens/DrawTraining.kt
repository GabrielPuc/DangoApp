package com.gdbm.dangoapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdbm.dangoapp.managers.drawing.DrawCanvas
import com.gdbm.dangoapp.managers.drawing.DrawingManager
import com.gdbm.dangoapp.ui.components.DrawScreenContent
import com.gdbm.dangoapp.ui.components.common.CardFace
import com.gdbm.dangoapp.ui.components.common.FlippableCard
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.components.common.PracticeCardContent
import com.gdbm.dangoapp.ui.components.common.ResizableText
import com.gdbm.dangoapp.ui.components.common.TextSizeRange
import com.gdbm.dangoapp.ui.components.dialogs.ContentSelectorDialog
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawTraining(
    drawingManager: DrawingManager,
    contentTrainingViewModel: ContentTrainingViewModel,
    screenTitle: String,
    isExperimentalEnabled: Boolean,
    contentType: String
) {

    val undoVisibility = remember { mutableStateOf(false) }
    val redoVisibility = remember { mutableStateOf(false) }
    val openSelectorDialog = remember { mutableStateOf(false) }
    var currentOrientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    var sliderPosition by remember { mutableFloatStateOf(80f) }
    var cardFace by remember { mutableStateOf(CardFace.Front) }
    var currentSymbol by remember { mutableStateOf(contentTrainingViewModel.getRandomWord()) }
    val canvasSize = if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
        LocalConfiguration.current.screenHeightDp
    } else {
        LocalConfiguration.current.screenWidthDp
    }

    Scaffold(
        topBar = {
            NavBar(title = screenTitle) {
                IconButton(onClick = {
                    openSelectorDialog.value = true
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = null, tint = CustomColorsPalette.current.textColor)
                }
            }

        }
    ) { innerPadding ->

        currentOrientation = LocalConfiguration.current.orientation
        DrawScreenContent(currentOrientation = currentOrientation, innerPadding = innerPadding, card = {
            var widthModifier = Modifier
                .height(140.dp)
                .padding(10.dp)
            widthModifier = if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
                widthModifier.then(Modifier.width(140.dp))
            }else{
                widthModifier.then(Modifier.wrapContentWidth())
            }
            FlippableCard(modifier = widthModifier,
                cardFace = cardFace,
                onClick = { if(!isExperimentalEnabled) cardFace = cardFace.flip },
                front = {
                    PracticeCardContent(
                        word = currentSymbol.meaning,
                        type = currentSymbol.type!!,
                        mainMinSize = 40,
                        mainMaxSize = 80,
                        mainStepSize = 10
                    )
                },
                back = {
                    PracticeCardContent(
                        word = currentSymbol.symbol,
                        type = currentSymbol.type!!,
                        mainMinSize = 40,
                        mainMaxSize = 80,
                        mainStepSize = 10
                    )
                })
        }, drawingPad = {

            Box(contentAlignment = Alignment.Center) {

                DrawCanvas(drawingManager = drawingManager,
                    backgroundColor = CustomColorsPalette.current.backgroundCanvas,
                    modifier = Modifier
                        .width(canvasSize.dp)
                        .height(canvasSize.dp),
                    onSaveBitmap = { imageBitmap, error ->
                        imageBitmap?.let {
                            contentTrainingViewModel.analyzeImage(
                                it.asAndroidBitmap(), currentSymbol
                            )
                        }
                    }) { undoCount, redoCount ->
                    undoVisibility.value = undoCount != 0
                    redoVisibility.value = redoCount != 0
                }

                if(!isExperimentalEnabled){
                    ResizableText(
                        text = currentSymbol.symbol,
                        maxLines = 1,
                        modifier = Modifier
                            .width(canvasSize.dp)
                            .height(canvasSize.dp)
                            .alpha(0.5f)
                            .align(Alignment.Center),
                        textSizeRange = TextSizeRange(
                            min = 80.sp,
                            max = canvasSize.sp,
                            step = 20.sp
                        ),
                        color = CustomColorsPalette.current.textColor,
                        overflow = TextOverflow.Visible,
                        style = MaterialTheme.typography.body1.copy(background = Color.Transparent),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }, brushSize = {
            Slider(
                value = sliderPosition,
                steps = 3,
                valueRange = 20f .. 80f,
                onValueChange = { sliderPosition = it; drawingManager.changeStrokeWidth(it) },
                modifier = Modifier
                    .width((canvasSize - 60).dp)
                    .height(10.dp),
                colors = SliderDefaults.colors(
                    thumbColor = CustomColorsPalette.current.contrastColor,
                    disabledThumbColor = CustomColorsPalette.current.secondaryContainerColor,
                    activeTrackColor = CustomColorsPalette.current.primaryContainerColor,
                    inactiveTrackColor = CustomColorsPalette.current.secondaryContainerColor,
                )
            )
        }, drawingControlBar = {
            DrawingControlsBar(
                drawController = drawingManager,
                undoVisibility = undoVisibility,
                redoVisibility = redoVisibility,
                orientation = currentOrientation
            )

            Spacer(modifier = Modifier.width(10.dp))

            SymbolsControlBar(drawController = drawingManager, nextPressedCallback = {
                cardFace = CardFace.Front
                drawingManager.reset()
                currentSymbol = contentTrainingViewModel.getRandomWord()
            }, flipCardCallback = {
                cardFace = cardFace.flip
            }, isExperimentalEnabled = isExperimentalEnabled,
                orientation = currentOrientation
            )
        })

        when {
            openSelectorDialog.value -> {
                ContentSelectorDialog(viewModel = contentTrainingViewModel, content=contentType, onDismissRequest = {
                    openSelectorDialog.value = false
                }) {
                    openSelectorDialog.value = false
                    contentTrainingViewModel.setWordsFromSelectedContent()
                    cardFace = CardFace.Front
                    drawingManager.reset()
                    currentSymbol = contentTrainingViewModel.getRandomWord()
                }
            }
        }

    }

}