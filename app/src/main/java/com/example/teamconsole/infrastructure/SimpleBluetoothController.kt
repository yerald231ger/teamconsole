package com.example.teamconsole.infrastructure

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.example.teamconsole.infrastructure.models.SppDevice
import com.example.teamconsole.infrastructure.receivers.FoundDeviceReceiver
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

@SuppressLint("MissingPermission")
class SimpleBluetoothController(private val context: Context) : BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _foundDeviceReceiver: FoundDeviceReceiver = FoundDeviceReceiver {
        _scannedDevices.tryEmit(it?.toBluetoothDevice())
    }

    private val _scannedDevices = MutableStateFlow<SppDevice?>(null)
    override val scannedDevices: StateFlow<SppDevice?> = _scannedDevices.asStateFlow()

    private val _boundedDevices = MutableStateFlow<List<SppDevice>?>(emptyList())
    override val boundedDevices: StateFlow<List<SppDevice>?> = _boundedDevices.asStateFlow()

    override fun startTest() {
        thread {
            runBlocking {
                for (i in 1..10) {
                    delay(1000)
                    _boundedDevices.update { devices ->
                        var newDevice = SppDevice("SppDevice[${i}]", i.toString())
                        devices?.plus(newDevice)
                    }
                }
            }
        }
    }

    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            throw SecurityException("No ${Manifest.permission.BLUETOOTH_SCAN} permission")

        val intent = IntentFilter(BluetoothDevice.ACTION_FOUND)
            .apply { addAction(BluetoothDevice.ACTION_UUID) }

        context.registerReceiver(_foundDeviceReceiver, intent)

        updateBoundedDevices()

        if (bluetoothAdapter?.isDiscovering == false)
            bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            throw SecurityException("No ${Manifest.permission.BLUETOOTH_SCAN} permission")

        bluetoothAdapter?.cancelDiscovery();
    }

    override fun release() {
        context.unregisterReceiver(_foundDeviceReceiver)
    }

    private fun updateBoundedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            throw SecurityException("No ${Manifest.permission.BLUETOOTH_CONNECT} permission")

        bluetoothAdapter
            ?.bondedDevices
            ?.map {
                it.toBluetoothDevice()
            }.also {
                _boundedDevices.update { it }
            }
    }

    private fun BluetoothDevice.toBluetoothDevice() =
        SppDevice(this.name, this.address)

    private fun hasPermission(permission: String) =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

}