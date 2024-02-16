package com.gdbm.dangoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdbm.dangoapp.R
import com.gdbm.dangoapp.ui.components.common.CardFace
import com.gdbm.dangoapp.ui.components.common.FlippableCard
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.components.common.ResizableText
import com.gdbm.dangoapp.ui.components.common.TextSizeRange
import com.gdbm.dangoapp.ui.components.dialogs.ContentSelectorDialog
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.GreenBackground
import com.gdbm.dangoapp.ui.theme.MainColor
import com.gdbm.dangoapp.ui.theme.RedWarning
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel
import java.util.Timer
import kotlin.concurrent.schedule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTraining(
    screenTitle:String,
    contentTrainingViewModel:ContentTrainingViewModel,
    shouldShowFilter:Boolean,
    contentType:String
){

    val cardFace by remember {
        mutableStateOf(CardFace.Front)
    }

    var wordOptions by remember {
        mutableStateOf(contentTrainingViewModel.getRandomWordWithOptions())
    }

    var correctAnswerIndex by remember {
        mutableStateOf((0..3).random())
    }


    var selected by remember {
        mutableStateOf("")
    }


    val openSelectorDialog= remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            NavBar(title = screenTitle) {

                if(shouldShowFilter){
                    IconButton(onClick = {
                        openSelectorDialog.value = true
                    }) {
                        Icon(Icons.Filled.Edit, contentDescription = null, tint = CustomColorsPalette.current.textColor)
                    }
                }


            }

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(1f)
                .background(CustomColorsPalette.current.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            FlippableCard(
                modifier = Modifier
                    .size(width = 300.dp, height = 300.dp)
                    .padding(10.dp),
                cardFace = cardFace,
                onClick = {/*cardFace = cardFace.flip*/},
                front = {
                    Box(contentAlignment = Alignment.Center){
                        ResizableText(
                            text = wordOptions[correctAnswerIndex].symbol,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .height(140.dp),
                            textSizeRange = TextSizeRange(
                                min = 40.sp,
                                max = 140.sp,
                                step = 20.sp
                            ),
                            color = CustomColorsPalette.current.textColor,
                            overflow = TextOverflow.Visible,
                            style = MaterialTheme.typography.body1,
                        )

                    }

                },
                back = {
                    Box(contentAlignment = Alignment.Center){
                        ResizableText(
                            text = wordOptions[correctAnswerIndex].meaning,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth(1f)
                                .align(Alignment.Center),
                            textSizeRange = TextSizeRange(
                                min = 40.sp,
                                max = 140.sp,
                                step = 20.sp
                            ),
                            color = CustomColorsPalette.current.textColor,
                            overflow = TextOverflow.Visible,
                            style = MaterialTheme.typography.body1,
                        )
                    }

                })
            
            LazyVerticalGrid(columns = GridCells.Adaptive(200.dp), modifier = Modifier
                .padding(top = 100.dp)
                .widthIn(max = (200 * wordOptions.size).dp)){
                if(wordOptions.isNotEmpty()){
                    wordOptions.iterator().forEach {
                        item {
                            Button(
                                onClick = {
                                    selected = it.symbol
                                    Timer("SettingUp", false).schedule(1000) {
                                        selected = ""
                                        wordOptions = contentTrainingViewModel.getRandomWordWithOptions()
                                        correctAnswerIndex = (0..3).random()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = setProperColor(selected, wordOptions[correctAnswerIndex].symbol, it.symbol),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.padding(10.dp)
                            ) {
                                ResizableText(
                                    text = it.meaning,
                                    maxLines = 1,
                                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                                    textSizeRange = TextSizeRange(
                                        min = 12.sp,
                                        max = 22.sp,
                                        step = 2.sp
                                    ),
                                    color = CustomColorsPalette.current.textColor,
                                    overflow = TextOverflow.Visible,
                                    style = MaterialTheme.typography.body1,
                                )
                            }
                        }
                    }
                }
            }
        }

        when {
            openSelectorDialog.value -> {
                ContentSelectorDialog(viewModel = contentTrainingViewModel, content=contentType ,onDismissRequest = {
                    openSelectorDialog.value = false
                }) {

                    openSelectorDialog.value = false
                    contentTrainingViewModel.setWordsFromSelectedContent()
                    wordOptions = contentTrainingViewModel.getRandomWordWithOptions()
                    correctAnswerIndex = (0..3).random()

                }
            }
        }
    }
}

private fun setProperColor(selected:String, correct:String, current:String): Color {
    if(selected.isBlank()){
        return MainColor
    }
    return if((selected == correct) && (selected == current)){
        GreenBackground
    }else if((selected != correct) && (selected == current)) {
        RedWarning
    }else if(current == correct){
        GreenBackground
    }else{
        MainColor
    }
}