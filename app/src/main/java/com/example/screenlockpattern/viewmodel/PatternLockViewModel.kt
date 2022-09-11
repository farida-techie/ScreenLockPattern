package com.example.screenlockpattern.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.screenlockpattern.componet.PatternViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PatternLockViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow<PatternViewState>(PatternViewState.Initial)
    val viewState: StateFlow<PatternViewState>
        get() = _viewState

    fun updateViewState(viewState: PatternViewState) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.emit(viewState)
        }
    }
}