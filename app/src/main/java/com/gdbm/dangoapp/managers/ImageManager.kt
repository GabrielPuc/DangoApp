package com.gdbm.dangoapp.managers

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gdbm.dangoapp.model.JapaneseWord
import com.gdbm.dangoapp.model.Word
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import java.io.File
import java.io.FileOutputStream

class ImageManager (val context: Context, private val textRecognizer: TextRecognizer) {

    private val resultStatus = MutableLiveData<Int>()

    fun analyzeImageWith(drawing: Bitmap, symbol: Word):Int{
        val scaled = scaleDownBitmapByMaxSize(drawing,480)
        saveDrawing(scaled,context)
        val inputImage = InputImage.fromBitmap(scaled!!, 0)
        val result = textRecognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                Log.d("VISION TEXT",visionText.text)
                if(symbol.symbol == visionText.text){
                    resultStatus.value = ContentTrainingViewModel.CORRECT
                }else{
                    resultStatus.value = ContentTrainingViewModel.WRONG
                }
            }
            .addOnFailureListener { e ->
                resultStatus.value = ContentTrainingViewModel.ERROR
                Log.e("TEXT RECOGNIZER", e.toString())
            }
        return resultStatus.value ?: 0
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
}