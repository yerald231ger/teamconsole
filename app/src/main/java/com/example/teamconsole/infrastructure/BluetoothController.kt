package com.example.teamconsole.infrastructure

import com.example.teamconsole.infrastructure.models.SppDevice
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val boundDevices : StateFlow<List<SppDevice>>
    val foundDevices : StateFlow<SppDevice>

    fun startDiscovery()
    fun stopDiscovery()
    fun release()

    fun test()
}