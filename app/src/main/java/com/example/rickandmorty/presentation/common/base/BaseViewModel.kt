package com.example.rickandmorty.presentation.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.presentation.common.event.DialogEvent
import com.example.rickandmorty.presentation.common.event.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel() : ViewModel() {

    private val _uiEvent = Channel<UiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _dialogEvent = Channel<DialogEvent>(Channel.BUFFERED)
    val  dialogEvent = _dialogEvent.receiveAsFlow()
    
    protected fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    
    protected fun sendDialogEvent(event: DialogEvent){
        viewModelScope.launch { 
            _dialogEvent.send(event)
        }
    }
    
    protected fun showError (title: String, message: String) {
        sendDialogEvent(DialogEvent.ShowError(title = title, message = message))
    }    
    
    protected fun showSnackBar(message: String) {
        sendUiEvent(UiEvent.ShowSnackBar(message = message))
    }
    
}