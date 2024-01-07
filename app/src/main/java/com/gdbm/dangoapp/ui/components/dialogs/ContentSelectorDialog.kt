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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gdbm.dangoapp.utils.Configs
import com.gdbm.dangoapp.ui.components.items.SettingItem
import com.gdbm.dangoapp.viewmodel.ContentTrainingViewModel

@Composable
fun ContentSelectorDialog(
    viewModel: ContentTrainingViewModel,
    content:String,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val items = when (content){
        Configs.SYLLABARY_CONTENT -> {
            Configs.SYLLABARY_ITEMS
        }
        Configs.VOCABULARY_CONTENT -> {
            Configs.VOCABULARY_ITEMS
        }
        else -> {
            emptyList()
        }
    }
    val scrollState = rememberScrollState()
    val dialogSize  = (LocalConfiguration.current.screenHeightDp * ((0.1) * items.size)).dp
    val word = mutableListOf<String>()
    val contentSelected = viewModel.selectedContentTypes.collectAsState()
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
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Column {
                    items.iterator().forEach {
                        val typeName = it
                        SettingItem(typeName , viewModel.containsContentType(typeName)) { selected ->
                            if(selected){
                                word.add(typeName)
                                viewModel.addContentTypeToSelected(typeName )
                            }else{
                                word.remove(typeName)
                                viewModel.removeContentTypeFromSelected(typeName )
                            }
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
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                        enabled = contentSelected.value.isNotEmpty(),
                    ) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}