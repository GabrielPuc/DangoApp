package com.gdbm.dangoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val mainScreenEventsChannel = Channel<Action>()
    val menuItemsEventsFlow = mainScreenEventsChannel.receiveAsFlow()

    val actionSelected = MutableLiveData<String>()
    private val _itsLoading = MutableStateFlow(false)
    val itsLoading: StateFlow<Boolean>
        get() = _itsLoading


    fun contentItsLoading(value:Boolean) {
        _itsLoading.value = value
    }

    fun select(option:String) {
        viewModelScope.launch {
            mainScreenEventsChannel.send(Action(option))
        }
    }

}

data class Action(val action: String)