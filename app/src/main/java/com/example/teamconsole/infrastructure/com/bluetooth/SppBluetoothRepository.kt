package com.example.teamconsole.infrastructure.com.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@SuppressLint("MissingPermission")
class SppBluetoothRepository(private val bluetoothAdapter: BluetoothAdapter) : BluetoothRepository {

    override fun getPairedDevices(): Flow<BluetoothDevice> = flow {
        for (bluetoothDevice in bluetoothAdapter.bondedDevices)
            emit(bluetoothDevice)
    }

    override fun getScannedDevices(): Flow<BluetoothDevice> {
        TODO("Not yet implemented")
    }
}