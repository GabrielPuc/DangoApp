package com.gdbm.dangoapp.ui.components.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gdbm.dangoapp.ui.theme.BackgroundColor
import com.gdbm.dangoapp.ui.theme.TextColor


@Composable
fun ExpandableView(
    sectionName: String,
    icon: Int = 0,
    iconColor: Color = TextColor,
    rotateAngle: Float = 0f,
    action: () -> Unit = {},
    additionalControl: () -> Unit = {},
    defaultState: Boolean = false,
    hasAction: Boolean = false,
    borderForCell: BorderStroke = BorderStroke(0.dp, Color.Transparent),
    headerColor: Color = Color.Transparent,
    verticalPaddingForCell: Dp = 8.dp,
    horizontalPaddingForCell: Dp = 16.dp,
    backgroundForCell: Color = BackgroundColor,
    backgroundForContent: Color = Color.Transparent,
    textColorForCell: Color = TextColor,
    headerHeight: Dp = 44.dp,
    content: @Composable () -> Unit,
) {

    var expandedState by remember { mutableStateOf(defaultState) }
    val rotationValue = if(rotateAngle != 0f){
        rotateAngle
    }else{
        180f
    }
    val rotationState by animateFloatAsState(targetValue = if (expandedState) rotationValue else 0f)

    val clickAction: () -> Unit = if(hasAction){
        action
    }else{
        {
            additionalControl.invoke()
            expandedState = !expandedState
        }
    }

    Column {
        Box(modifier = Modifier
            .background(headerColor)
            .border(width = 1.dp, color = headerColor, shape = RectangleShape)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        top = verticalPaddingForCell,
                        bottom = verticalPaddingForCell,
                        start = horizontalPaddingForCell,
                        end = horizontalPaddingForCell
                    )
                    .height(headerHeight)
                    .clickable(
                        onClick = {
                            clickAction.invoke()
                        }
                    )
                    .background(color = headerColor)
            ) {
                Text(
                    text = sectionName,
                    color = textColorForCell,
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier
                        .weight(1f),
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .rotate(rotationState),
                    onClick = clickAction
                ) {
                    if(icon != 0){
                        Icon(painter = painterResource(id = icon), contentDescription = "",tint = iconColor)
                    }else{
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "",tint = iconColor)
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearOutSlowInEasing
                    )
                ),
            shape = RoundedCornerShape(0.dp),
            //onClick = clickAction,
            border = borderForCell
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundForCell)
            ) {
                if (expandedState) {
                    Box(modifier = Modifier.background(backgroundForContent)) {
                        content()
                    }
                }

            }
        }

    }

}