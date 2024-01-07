package com.gdbm.dangoapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val actionSelected = MutableLiveData<String>()
    private val _itsLoading = MutableStateFlow(false)
    val itsLoading: StateFlow<Boolean>
        get() = _itsLoading


    fun contentItsLoading(value:Boolean) {
        _itsLoading.value = value
    }

}