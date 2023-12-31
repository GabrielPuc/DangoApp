package com.gdbm.dangoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gdbm.dangoapp.R
import com.gdbm.dangoapp.config.Configs
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.ui.screens.Main
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.JapaneseTrainerTheme
import com.gdbm.dangoapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel:MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel =
            ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.loadAllContent(this@MainActivity)
        val contentManager = ContentManager.getInstance(applicationContext)
        contentManager.loadAllContent()
        setContent {
            JapaneseTrainerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background
                ) {
                    Main(mainViewModel = mainViewModel, screenTitle = this@MainActivity.resources.getString(
                        R.string.app_name))
                }
            }
        }

        mainViewModel.actionSelected.observe(this, Observer { selected ->
            val optionSelected = Configs.MENU_OPTIONS.firstOrNull { element-> element.name == selected }
            optionSelected?.let {
                val intent = Intent(this@MainActivity,optionSelected.java as Class<*>)
                optionSelected.content?.let {
                    intent.putExtra("CONTENT",it)
                }
                startActivity(intent)
            }
        })
    }

    @Override
    override fun onPause() {
        super.onPause()
        mainViewModel.actionSelected.value = ""
    }
}