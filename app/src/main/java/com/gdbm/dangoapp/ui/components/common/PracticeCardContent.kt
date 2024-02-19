package com.gdbm.dangoapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdbm.dangoapp.ui.theme.CustomColorsPalette
import com.gdbm.dangoapp.utils.extensions.conditional

@Composable
fun PracticeCardContent(word:String, type:String, mainMinSize:Int = 40, mainMaxSize:Int = 140, mainStepSize:Int = 20, customHeight:Int? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        ResizableText(
            text = word,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth(1f)
                .conditional(customHeight != null) {
                    height(customHeight!!.dp)
                },
            textSizeRange = TextSizeRange(
                min = mainMinSize.sp,
                max = mainMaxSize.sp,
                step = mainStepSize.sp
            ),
            color = CustomColorsPalette.current.textColor,
            overflow = TextOverflow.Visible,
            style = MaterialTheme.typography.body1,
        )
        Text(text = "($type)",
            modifier = Modifier
                .fillMaxWidth(1f), textAlign = TextAlign.Center)
    }
}