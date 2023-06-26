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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class SimpleBluetoothController(private val context: Context) : BluetoothController {

    private val tag = "SimpleBluetoothController"
    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val _foundDeviceReceiver: FoundDeviceReceiver = FoundDeviceReceiver {
            _foundDevices.tryEmit(it?.toBluetoothDevice())
    }

    private val _foundDevices = MutableStateFlow<SppDevice?>(null)
    override val foundDevices: StateFlow<SppDevice?> = _foundDevices.asStateFlow()

    private val _boundDevices = MutableStateFlow<List<SppDevice>>(emptyList())
    override val boundDevices: StateFlow<List<SppDevice>> = _boundDevices.asStateFlow()

    init {
        updateBoundedDevices()
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

        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(_foundDeviceReceiver)
    }

    override fun test() {
        updateBoundedDevices()
    }

    private fun updateBoundedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            throw SecurityException("No ${Manifest.permission.BLUETOOTH_CONNECT} permission")

        bluetoothAdapter
            ?.bondedDevices
            ?.map {
                it.toBluetoothDevice()
            }
            .also { sppDevices ->
                if (sppDevices is List<SppDevice>)
                    _boundDevices.update{ sppDevices }
            }
    }

    private fun BluetoothDevice.toBluetoothDevice() : SppDevice =
        SppDevice(if (this.name is String) this.name else "Unknown device", this.address)

    private fun hasPermission(permission: String) =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

}