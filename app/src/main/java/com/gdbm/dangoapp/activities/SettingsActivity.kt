package com.gdbm.dangoapp.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gdbm.dangoapp.managers.ContentManager
import com.gdbm.dangoapp.ui.screens.SettingsDetail
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.DangoTheme
import com.gdbm.dangoapp.viewmodel.MainViewModel
import com.gdbm.dangoapp.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch


class SettingsActivity : ComponentActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var contentManager: ContentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentManager = ContentManager.getInstance(this)
        settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]
        setContent {
            //val isDarkMode = settingsViewModel.isDarkMode.collectAsState()
            DangoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CustomColorsPalette.current.background,
                ) {
                    SettingsDetail(settingsViewModel= settingsViewModel)
                }
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                settingsViewModel.settingsEventsFlow.collect { selected ->
                    when(selected.action) {
                        "UPDATE" -> {
                            verifyContent()
                        }
                    }
                }
            }
        }
    }



    private fun setLoadingState(loading:Boolean) {
        if(loading) {
            settingsViewModel.contentItsLoading(true)
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            settingsViewModel.contentItsLoading(false)
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            Toast.makeText(this@SettingsActivity, "Content Updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyContent() {
        contentManager.contentAvailable()?.let {
            setLoadingState(true)
            contentManager.updateContent(it)
            setLoadingState(false)
        } ?: kotlin.run {
            setLoadingState(true)
            contentManager.fetchAllContent()
            setLoadingState(false)
        }
    }
}