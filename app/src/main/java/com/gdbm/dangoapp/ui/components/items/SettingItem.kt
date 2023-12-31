package com.gdbm.dangoapp.ui.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import com.gdbm.dangoapp.ui.theme.GreenOk
import com.gdbm.dangoapp.ui.theme.TextColor

@Composable
fun SettingItem(typeName:String, selected:Boolean, callback: (Boolean) -> Unit) {

    val checkedStatus= remember { mutableStateOf(selected) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(1f)
            .semantics(true, {})
            .clickable {
                checkedStatus.value = !checkedStatus.value
                callback(checkedStatus.value)
    }){
        Checkbox(
            colors = CheckboxDefaults.colors(GreenOk),
            checked = checkedStatus.value,
            onCheckedChange = {checkedStatus.value = !checkedStatus.value;callback(checkedStatus.value)})
        Text(text = typeName, color = TextColor)
    }
}