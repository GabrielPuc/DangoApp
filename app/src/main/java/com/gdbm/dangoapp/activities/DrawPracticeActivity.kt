package com.gdbm.dangoapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.managers.ImageManager
import com.gdbm.dangoapp.managers.drawing.rememberDrawingManager
import com.gdbm.dangoapp.ui.screens.DrawTraining
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.DangoTheme
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel.Companion.CORRECT
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel.Companion.ERROR
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel.Companion.WRONG
//import com.google.mlkit.vision.text.TextRecognition
//import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.googlecode.tesseract.android.TessBaseAPI


class DrawPracticeActivity : ComponentActivity() {

    private var contentType:String = ""
    private var tesseract:TessBaseAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isExperimentalEnabled = intent.getBooleanExtra("EXPERIMENTAL_ENABLED", false)
        contentType = intent.getStringExtra("CONTENT")!!
        val contentTrainingViewModel =
            ViewModelProvider(this)[ContentTrainingViewModel::class.java]
        val contentManager = ContentManager.getInstance(applicationContext)
        contentTrainingViewModel.setCurrentContentManager(contentManager)
        contentTrainingViewModel.createWordListFor(contentType)
        tesseract = TessBaseAPI()
        val imageManager = ImageManager(
            context = this@DrawPracticeActivity,
            textRecognizer = tesseract!!)
        imageManager.initTesseracts()

        setContent {
            DangoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background
                ) {
                    val drawManager = rememberDrawingManager()
                    drawManager.changeColor(CustomColorsPalette.current.textColor)
                    drawManager.changeStrokeWidth(80f)
                    DrawTraining(
                        drawingManager = drawManager,
                        contentTrainingViewModel = contentTrainingViewModel,
                        screenTitle = "Practice",
                        isExperimentalEnabled = isExperimentalEnabled,
                        contentType = contentType
                    )
                }
            }
        }

        contentTrainingViewModel.drawingPair.observe(this) { pair ->
            val imageAnalysisResult = imageManager.analyzeImageWith(pair.first, pair.second)
            val message = when (imageAnalysisResult.first) {
                CORRECT -> "Correct!"
                WRONG -> "Incorrect, Detected: ${imageAnalysisResult.second}"
                ERROR -> "Drawing not recognized"
                else -> ""
            }
            if(message.isNotBlank()) {
                Toast.makeText(this@DrawPracticeActivity,message,Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            tesseract!!.recycle()
        } catch (exception:Exception) {
            Log.e("TESSERACT",exception.message.toString())
        }
    }
}