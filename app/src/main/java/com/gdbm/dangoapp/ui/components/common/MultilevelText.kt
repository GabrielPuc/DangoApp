package com.gdbm.dangoapp.ui.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gdbm.dangoapp.ui.theme.PinkBackground

@Composable
fun MultilevelText(firstLevel:String, thirdLevel:String?=null, secondLevel:String?=null, cardWidth:Dp? = null){
    Column {
        Text(
            text = firstLevel,
            fontSize = 22.sp,
            modifier = Modifier
                .padding(6.dp).fillMaxWidth(1f),
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        secondLevel?.let {
            Text(
                text = secondLevel,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(6.dp).fillMaxWidth(1f),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        thirdLevel?.let {
            Text(
                text = thirdLevel,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(6.dp).fillMaxWidth(1f),
                textAlign = TextAlign.Center,
                color = PinkBackground,
                maxLines = 1
            )
        }
    }
}