package com.example.teamconsole.infrastructure.receivers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.UUID

class FoundDeviceReceiver : BroadcastReceiver() {

    private val tag = "FoundDeviceReceiver"
    private val _onFoundDevice = MutableSharedFlow<BluetoothDevice?>()
    val onFoundDevice = _onFoundDevice.asSharedFlow()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {

        val device: BluetoothDevice?
        when (intent.action) {
            BluetoothDevice.ACTION_UUID -> {
                val uuids = intent.getParcelableArrayExtra(
                    BluetoothDevice.EXTRA_UUID,
                    Array<UUID>::class.java
                )
                device = intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java
                )
                Log.i(tag, "Uuid: $uuids. Device = [${device?.name}][${device?.address}]")
                _onFoundDevice.tryEmit(device)
            }

            BluetoothDevice.ACTION_FOUND -> {
                device = intent.getParcelableExtra(
                    BluetoothDevice.EXTRA_DEVICE,
                    BluetoothDevice::class.java
                )
                Log.i(tag, "Uuid: Device = [${device?.name}][${device?.address}]")
                _onFoundDevice.tryEmit(device)
            }
        }

    }

}