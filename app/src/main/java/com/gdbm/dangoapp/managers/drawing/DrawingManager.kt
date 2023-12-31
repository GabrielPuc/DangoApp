package com.gdbm.dangoapp.managers.drawing

import android.graphics.Bitmap
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

class DrawingManager internal constructor(val history: (undoCount: Int, redoCount: Int) -> Unit = { _, _ -> }) {

    private val redoList = mutableStateListOf<PathWrapper>()
    private val undoList = mutableStateListOf<PathWrapper>()
    internal val pathList: SnapshotStateList<PathWrapper> = undoList


    private val _historyTracker = MutableSharedFlow<String>(extraBufferCapacity = 1)
    private val historyTracker = _historyTracker.asSharedFlow()

    fun trackHistory(
        scope: CoroutineScope,
        trackHistory: (undoCount: Int, redoCount: Int) -> Unit
    ) {
        historyTracker
            .onEach { trackHistory(undoList.size, redoList.size) }
            .launchIn(scope)
    }


    private val _bitmapGenerator = MutableSharedFlow<Bitmap.Config>(extraBufferCapacity = 1)
    private val bitmapGenerator = _bitmapGenerator.asSharedFlow()

    fun saveBitmap(config: Bitmap.Config = Bitmap.Config.ARGB_8888) =
        _bitmapGenerator.tryEmit(config)

    var opacity by mutableStateOf(1f)
        private set

    var strokeWidth by mutableStateOf(10f)
        private set

    var color by mutableStateOf(Color.Red)
        private set

    var bgColor by mutableStateOf(Color.Black)
        private set

    fun changeOpacity(value: Float) {
        opacity = value
    }

    fun changeColor(value: Color) {
        color = value
    }

    fun changeBgColor(value: Color) {
        bgColor = value
    }

    fun changeStrokeWidth(value: Float) {
        strokeWidth = value
    }

    fun importPath(drawBoxContent: DrawBoxContent) {
        reset()
        bgColor = drawBoxContent.bgColor
        undoList.addAll(drawBoxContent.path)
        _historyTracker.tryEmit("${undoList.size}")
    }

    fun exportPath() = DrawBoxContent(bgColor, pathList.toList())


    fun unDo() {
        if (undoList.isNotEmpty()) {
            val last = undoList.last()
            redoList.add(last)
            undoList.remove(last)
            history(undoList.size, redoList.size)
            _historyTracker.tryEmit("Undo - ${undoList.size}")
        }
    }

    fun reDo() {
        if (redoList.isNotEmpty()) {
            val last = redoList.last()
            undoList.add(last)
            redoList.remove(last)
            history(undoList.size, redoList.size)
            _historyTracker.tryEmit("Redo - ${redoList.size}")
        }
    }


    fun reset() {
        redoList.clear()
        undoList.clear()
        _historyTracker.tryEmit("-")
    }

    fun updateLatestPath(newPoint: Offset) {
        val index = undoList.lastIndex
        undoList[index].points.add(newPoint)
    }

    fun insertNewPath(newPoint: Offset) {
        val pathWrapper = PathWrapper(
            points = mutableStateListOf(newPoint),
            strokeColor = color,
            alpha = opacity,
            strokeWidth = strokeWidth,
        )
        undoList.add(pathWrapper)
        redoList.clear()
        _historyTracker.tryEmit("${undoList.size}")
    }

    fun trackBitmaps(
        it: View,
        coroutineScope: CoroutineScope,
        onCaptured: (ImageBitmap?, Throwable?) -> Unit
    ) = bitmapGenerator
        .mapNotNull { config -> it.drawBitmapFromView(it.context, config) }
        .onEach { bitmap -> onCaptured(bitmap.asImageBitmap(), null) }
        .catch { error -> onCaptured(null, error) }
        .launchIn(coroutineScope)
}

@Composable
fun rememberDrawingManager(): DrawingManager {
    return remember { DrawingManager() }
}