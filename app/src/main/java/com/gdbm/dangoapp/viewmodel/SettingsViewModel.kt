package com.gdbm.dangoapp.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val settingsEventsChannel = Channel<Action>()
    val settingsEventsFlow = settingsEventsChannel.receiveAsFlow()

    private val _itsLoading = MutableStateFlow(false)
    val itsLoading: StateFlow<Boolean>
        get() = _itsLoading


    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean>
        get() = _isDarkMode

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun contentItsLoading(value:Boolean) {
        _itsLoading.value = value
    }

    fun select(option:String) {
        viewModelScope.launch {
            settingsEventsChannel.send(Action(option))
        }
    }
}