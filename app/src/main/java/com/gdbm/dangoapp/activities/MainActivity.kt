package com.gdbm.dangoapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.gdbm.dangoapp.R
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.ui.screens.Main
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.DangoTheme
import com.gdbm.dangoapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel:MainViewModel
    private lateinit var contentManager: ContentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel =
            ViewModelProvider(this)[MainViewModel::class.java]
        contentManager = ContentManager.getInstance(this)
        verifyContent()
        setContent {
            DangoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background
                ) {
                    Main(mainViewModel = mainViewModel, screenTitle = this@MainActivity.resources.getString(
                        R.string.app_name))
                }
            }
        }

        mainViewModel.actionSelected.observe(this) { selected ->
            val optionSelected =
                Configs.MENU_OPTIONS.firstOrNull { element -> element.name == selected }
            optionSelected?.let {
                val intent = Intent(this@MainActivity, optionSelected.java as Class<*>)
                optionSelected.experimentalEnabled?.let {
                    intent.putExtra("EXPERIMENTAL_ENABLED", it)
                }
                optionSelected.content?.let {
                    intent.putExtra("CONTENT", it)
                }
                optionSelected.isLargeContent?.let {
                    intent.putExtra("LARGE_CONTENT", it)
                }
                startActivity(intent)
            }
        }
    }

    private fun verifyContent() {
        contentManager.contentAvailable()?.let {
            if(contentManager.needToUpdateFiles()) {
                mainViewModel.contentItsLoading(true)
                contentManager.updateContent(it)
                mainViewModel.contentItsLoading(false)
            }
            mainViewModel.contentItsLoading(false)
        } ?: kotlin.run {
            mainViewModel.contentItsLoading(true)
            contentManager.fetchAllContent()
            mainViewModel.contentItsLoading(false)
        }

    }

    @Override
    override fun onPause() {
        super.onPause()
        mainViewModel.actionSelected.value = ""
    }
}