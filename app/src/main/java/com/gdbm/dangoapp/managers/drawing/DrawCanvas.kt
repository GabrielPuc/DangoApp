package com.gdbm.dangoapp.managers.drawing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun DrawCanvas(
    drawingManager: DrawingManager,
    modifier: Modifier,
    backgroundColor: Color = Color.White,
    onSaveBitmap: (ImageBitmap?, Throwable?) -> Unit,
    drawnLinesHistory: (undoCount: Int, redoCount: Int) -> Unit = { _, _ -> }
) = AndroidView(
    factory = {
        ComposeView(it).apply {
            setContent {
                LaunchedEffect(drawingManager) {
                    drawingManager.changeBgColor(backgroundColor)
                    drawingManager.trackBitmaps(this@apply, this, onSaveBitmap)
                    drawingManager.trackHistory(this, drawnLinesHistory)
                }
                Canvas(modifier = modifier
                    .background(drawingManager.bgColor)
                    .clipToBounds()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                drawingManager.insertNewPath(offset)
                            }
                        ) { change, _ ->
                            val newPoint = change.position
                            drawingManager.updateLatestPath(newPoint)
                        }
                    }) {

                    drawingManager.pathList.forEach { pw ->
                        drawPath(
                            createPath(pw.points),
                            color = pw.strokeColor,
                            alpha = pw.alpha,
                            style = Stroke(
                                width = pw.strokeWidth,
                                cap = StrokeCap.Square,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }
        }
    },
    modifier = modifier
)