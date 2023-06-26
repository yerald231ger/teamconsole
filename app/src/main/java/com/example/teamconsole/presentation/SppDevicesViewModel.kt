package com.example.teamconsole.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teamconsole.infrastructure.BluetoothController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class SppDevicesViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
) : ViewModel() {

    private val _state = MutableStateFlow(SppDevicesUIState())
    val state = combine(bluetoothController.foundDevices, bluetoothController.boundDevices, _state)
    { foundDevices, boundDevices, state ->
        state.copy(
        foundSppDevices = foundDevices,
        boundSppDevices = boundDevices
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)
}