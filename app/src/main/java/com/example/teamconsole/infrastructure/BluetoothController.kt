package com.example.teamconsole.infrastructure

import com.example.teamconsole.infrastructure.models.SppDevice
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val pairedDevices : StateFlow<List<SppDevice>>
    val boundedDevices : StateFlow<SppDevice?>

    fun startDiscovery()
    fun stopDiscovery()

    fun release()
}