package com.gdbm.dangoapp.managers

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.gdbm.dangoapp.model.Word
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel
//import com.google.mlkit.vision.common.InputImage
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream

class ImageManager (val context: Context, private val textRecognizer: TessBaseAPI) {

    private val resultStatus = MutableLiveData<Pair<Int,String?>>()

    fun analyzeImageWith(drawing: Bitmap, symbol: Word):Pair<Int,String?>{
        val scaled = scaleDownBitmapByMaxSize(drawing,480)
        textRecognizer.setImage(scaled)
        val result = textRecognizer.utF8Text
        resultStatus.value = when(result){
            symbol.symbol -> Pair(ContentTrainingViewModel.CORRECT, null)
            "" -> Pair(ContentTrainingViewModel.ERROR, null)
            else -> Pair(ContentTrainingViewModel.WRONG, result)
        }
        return resultStatus.value ?: Pair(ContentTrainingViewModel.ERROR,null)
    }

    private fun saveDrawing(bitmap: Bitmap?, context: Context) {
        val path = "${context.filesDir.path}/drawings"
        File(path).let {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        val filename = "$path/drawing.png"
        if (bitmap != null) {
            FileOutputStream(filename).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, `out`)
            }
        } else {
            File(filename).delete()
        }
    }

    private fun scaleDownBitmapByMaxSize(bitmap: Bitmap?, max: Int): Bitmap? {
        if (bitmap == null) {
            return null
        }
        var width = bitmap.width
        var height = bitmap.height
        if (width > height) {
            if (width <= max) {
                return bitmap
            }
            val ratio = width.toFloat() / max
            height = (height / ratio).toInt()
            width = max
        } else if (height > width) {
            if (height <= max) {
                return bitmap
            }
            val ratio = height.toFloat() / max
            width = (width / ratio).toInt()
            height = max
        } else {
            height = max
            width = max
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }

    fun initTesseracts() {
        val dataPath = File(context.filesDir, "").absolutePath
        if (!textRecognizer.init(dataPath, "jpn")) {
            textRecognizer.recycle();
            return;
        }
    }
}