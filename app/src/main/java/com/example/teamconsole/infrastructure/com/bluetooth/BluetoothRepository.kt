package com.example.teamconsole.infrastructure.com.bluetooth

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {
    fun getPairedDevices() : Flow<BluetoothDevice>
    fun getScannedDevices() : Flow<BluetoothDevice>
}