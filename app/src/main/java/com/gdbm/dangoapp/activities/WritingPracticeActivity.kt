package com.gdbm.dangoapp.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.ui.screens.WriteTraining
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.JapaneseTrainerTheme
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel

class WritingPracticeActivity : ComponentActivity() {

    private var contentType:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentType = intent.getStringExtra("CONTENT")
        val contentTrainingViewModel =
            ViewModelProvider(this)[ContentTrainingViewModel::class.java]
        val contentManager = ContentManager.getInstance(applicationContext)
        contentTrainingViewModel.setCurrentContentManager(contentManager)
        contentType?.let {
            contentTrainingViewModel.setDefaultVocabulary()
            contentTrainingViewModel.setVocabulary()
        } ?: run {
            contentTrainingViewModel.setDefaultGeneral()
            contentTrainingViewModel.setGeneralWords()
        }
        setContent {
            JapaneseTrainerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background
                ) {
                    WriteTraining(
                        screenTitle = "Practice",
                        contentTrainingViewModel = contentTrainingViewModel,
                        shouldShowFilter = contentType != ""
                    )
                }
            }
        }
    }
}