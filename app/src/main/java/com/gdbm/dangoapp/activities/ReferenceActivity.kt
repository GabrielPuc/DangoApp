package com.gdbm.dangoapp.activities

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.gdbm.dangoapp.config.Configs
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.managers.FileManager
import com.gdbm.dangoapp.model.Word
import com.gdbm.dangoapp.ui.screens.Reference
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.DangoTheme
import java.util.Locale

class ReferenceActivity : ComponentActivity() {

    private var contentType = ""
    private var largeContent = false
    private var screenTitle = ""
    private var content:Map<String,List<Word>> = emptyMap()
    private lateinit var tts:TextToSpeech
    private lateinit var fileManager:FileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileManager = FileManager(this@ReferenceActivity, Configs.CURRENT_LANGUAGE)
        contentType = intent.getStringExtra("CONTENT")!!
        largeContent = intent.getBooleanExtra("LARGE_CONTENT",false)
        tts = TextToSpeech(this@ReferenceActivity){
            if(it == TextToSpeech.SUCCESS){
                tts.language = Locale.JAPAN
                tts.setSpeechRate(0.8f)
            }
        }

        setContent {
            DangoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background
                ) {
                    Reference(content = content, screenTitle, largeContent, tts)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()

        val optionSelected = Configs.MENU_OPTIONS.firstOrNull { element-> element.content == contentType }
        optionSelected?.let {
            content = ContentManager.getInstance(applicationContext).retrieve(optionSelected.content!!)!!.groupBy{ it.group }
            screenTitle = optionSelected.name
        }

    }
}