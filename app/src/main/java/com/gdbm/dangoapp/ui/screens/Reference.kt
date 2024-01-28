package com.gdbm.dangoapp.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gdbm.dangoapp.model.Word
import com.gdbm.dangoapp.ui.components.common.MultilevelText
import com.gdbm.dangoapp.ui.components.common.NavBar
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.ui.theme.GreenBackground
import com.gdbm.dangoapp.ui.theme.TextColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Reference(
    content: Map<String, List<Word>>,
    screenTitle: String,
    largeContent: Boolean = false,
    tts: TextToSpeech
) {
    val widthOfItem = if (largeContent) {
        200
    } else {
        100
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp

    val indexSelected = remember { mutableStateListOf<Int>() }
    val contentToShow = remember {
        content
    }


    Scaffold(topBar = { NavBar(title = screenTitle) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(1f)
        ) {
            val itemsInRow = screenWidth / widthOfItem

            LazyColumn() {
                contentToShow.toList().forEachIndexed { index, entry ->
                    stickyHeader {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .height(44.dp)
                                    .clickable {
                                        if (indexSelected.contains(index)) {
                                            indexSelected.remove(index)
                                        } else {
                                            indexSelected.add(index)
                                        }
                                    }
                                    .background(color = GreenBackground)) {
                                Text(
                                    text = entry.first,
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(6.dp),
                                    overflow = TextOverflow.Ellipsis
                                )
                                IconButton(modifier = Modifier.rotate(
                                        if (indexSelected.contains(index)) {
                                            180f
                                        } else {
                                            0f
                                        }
                                    ), onClick = {
                                    if (indexSelected.contains(index)) {
                                        indexSelected.remove(index)
                                    } else {
                                        indexSelected.add(index)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "",
                                        tint = TextColor
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().animateContentSize(
                                animationSpec = tween(
                                    durationMillis = 200, easing = LinearOutSlowInEasing
                                )
                            )
                        ) {
                            if (indexSelected.contains(index)) {
                                Box(modifier = Modifier.background(CustomColorsPalette.current.background)) {

                                    Column {
                                        entry.second.chunked(itemsInRow).iterator()
                                            .forEach { chunk ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(1f),
                                                    horizontalArrangement = Arrangement.SpaceEvenly
                                                ) {
                                                    chunk.iterator().forEach { word ->
                                                        ElevatedCard(modifier = Modifier
                                                            .width(
                                                                if (itemsInRow == 1) {
                                                                    screenWidth.dp
                                                                } else {
                                                                    widthOfItem.dp
                                                                }
                                                            )
                                                            .padding(10.dp), onClick = {
                                                            word.pronunciation?.let {
                                                                tts.speak(
                                                                    word.pronunciation,
                                                                    TextToSpeech.QUEUE_ADD,
                                                                    null,
                                                                    ""
                                                                )
                                                            } ?: run {
                                                                tts.speak(
                                                                    word.symbol,
                                                                    TextToSpeech.QUEUE_ADD,
                                                                    null,
                                                                    ""
                                                                )
                                                            }
                                                        }) {
                                                            Icon(Icons.Filled.PlayArrow, "play")
                                                            MultilevelText(
                                                                firstLevel = word.symbol,
                                                                secondLevel = word.meaning,
                                                                thirdLevel = word.latinBased,
                                                                cardWidth = widthOfItem.dp
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                    }
                                }
                            }

                        }
                    }

                }
            }

        }
    }
}