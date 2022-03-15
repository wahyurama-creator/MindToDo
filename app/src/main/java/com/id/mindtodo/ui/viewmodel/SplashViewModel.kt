package com.id.mindtodo.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.id.mindtodo.data.repository.DataStoreOnBoardRepository
import com.id.mindtodo.ui.util.Constants.LIST_SCREEN_NO_ACTION
import com.id.mindtodo.ui.util.Constants.ON_BOARD_SCREEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val repository: DataStoreOnBoardRepository
) : ViewModel() {

    private val _startDestination: MutableState<String> = mutableStateOf(ON_BOARD_SCREEN)
    val startDestination: State<String> = _startDestination

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.readOnBoardingState().collect { completed ->
                if (completed) {
                    _startDestination.value = LIST_SCREEN_NO_ACTION
                } else {
                    _startDestination.value = ON_BOARD_SCREEN
                }
            }
        }
    }
}