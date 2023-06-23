package com.example.teamconsole.infrastructure

import com.example.teamconsole.infrastructure.models.SppDevice
import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val boundedDevices : StateFlow<List<SppDevice>?>
    val scannedDevices : StateFlow<SppDevice?>

    fun startDiscovery()
    fun stopDiscovery()
    fun release()

    fun startTest()
}