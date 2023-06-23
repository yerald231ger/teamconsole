package com.example.teamconsole.infrastructure.receivers

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID

class FoundDeviceReceiver(val callback: (BluetoothDevice?) -> Unit) : BroadcastReceiver() {

    private val tag = "FoundDeviceReceiver"

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            BluetoothDevice.ACTION_UUID -> {
                val uuids = intent.getParcelableArrayExtra(BluetoothDevice.EXTRA_UUID, Array<UUID>::class.java)
                val device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                Log.i(tag, "Uuid: $uuids. Device = [${device?.name}][${device?.address}]")
                callback(device)
            }

            BluetoothDevice.ACTION_FOUND -> {
                val device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE,BluetoothDevice::class.java)
                Log.i(tag, "Uuid: Device = [${device?.name}][${device?.address}]")
                callback(device)
            }
        }
    }
}