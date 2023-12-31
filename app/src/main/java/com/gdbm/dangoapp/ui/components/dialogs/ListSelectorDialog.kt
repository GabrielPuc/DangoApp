package com.gdbm.dangoapp.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gdbm.dangoapp.ui.components.items.SettingItem

import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel

@Composable
fun ListSelectorDialog(
    viewModel: ContentTrainingViewModel,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val dialogSize  = (LocalConfiguration.current.screenWidthDp * 1.5).dp
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dialogSize)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                val allGroups = viewModel.contentManager.getAllContentGroups()

                allGroups.iterator().forEach {
                    val typeName = it.key
                    SettingItem(typeName , viewModel.containsGroup(typeName )) { selected ->
                        if(selected){
                            viewModel.addToSelected(typeName )
                        }else{
                            viewModel.removeFromSelected(typeName )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}