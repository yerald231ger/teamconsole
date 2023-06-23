package com.example.teamconsole.infrastructure

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import com.example.teamconsole.infrastructure.models.SppDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class SimpleBluetoothController(private val context: Context) : BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _pairedDevices = MutableStateFlow<List<SppDevice>>(emptyList())
    override val pairedDevices: StateFlow<List<SppDevice>> = _pairedDevices.asStateFlow()

    private val _boundedDevices = MutableStateFlow<SppDevice?>(null)
    override val boundedDevices: StateFlow<SppDevice?> = _boundedDevices.asStateFlow()

    override fun startDiscovery() {
        TODO("Not yet implemented")
    }

    override fun stopDiscovery() {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    private fun updateBoundedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            throw SecurityException("No ${Manifest.permission.BLUETOOTH_CONNECT} permission")

        bluetoothAdapter
            ?.bondedDevices
            ?.map { bd -> bd.toBluetoothDevice() }
            .also {
                _boundedDevices.update { it }
            }
    }

    private fun android.bluetooth.BluetoothDevice.toBluetoothDevice() =
        SppDevice(this.name, this.address)

    private fun hasPermission(permission: String) =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

}