package com.example.teamconsole

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teamconsole.databinding.ActivityMainBinding
import com.example.teamconsole.infrastructure.BluetoothController
import com.example.teamconsole.infrastructure.models.SppDevice
import com.example.teamconsole.models.DeviceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val foundSppDevices: MutableList<SppDevice> = emptyList<SppDevice>().toMutableList()
    private val boundSppDevices: MutableList<SppDevice> = emptyList<SppDevice>().toMutableList()
    private val isBluetoothEnabled: Boolean get() = bluetoothAdapter?.isEnabled == true

    @Inject
    lateinit var bluetoothController: BluetoothController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestBluetoothEnableLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK)
                    enableContent()
                else
                    disabledContent()
            }

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions[BLUETOOTH_SCAN] == false || permissions[BLUETOOTH_CONNECT] == false)
                    disabledContent()
                else {
                    if (!isBluetoothEnabled)
                        requestBluetoothEnableLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                    else
                        enableContent()
                }
            }

        requestPermissionLauncher.launch(arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT))
        Log.i(tag, getString(R.string.activity_created))
    }

    private fun enableContent() {
        binding.btnStartDiscover.setOnClickListener {
            bluetoothController.startDiscovery()
        }

        val foundSppDeviceAdapter = DeviceAdapter(foundSppDevices)
        binding.rvFoundDevices.adapter = foundSppDeviceAdapter
        binding.rvFoundDevices.layoutManager = LinearLayoutManager(this)

        val boundSppDeviceAdapter = DeviceAdapter(boundSppDevices)
        binding.rvBoundDevices.adapter = boundSppDeviceAdapter
        binding.rvBoundDevices.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            bluetoothController.boundDevices.collect {
                for (sppDevice in it) {
                    if (!boundSppDevices.contains(sppDevice)) {
                        boundSppDevices += sppDevice
                        boundSppDeviceAdapter.notifyItemInserted(boundSppDevices.size - 1)
                    }
                }
            }
        }

        lifecycleScope.launch {
            bluetoothController.foundDevices.collect {
                if (!boundSppDevices.contains(it) && it != null) {
                    foundSppDevices += it
                    foundSppDeviceAdapter.notifyItemInserted(foundSppDevices.size - 1)
                }
            }
        }
    }

    private fun disabledContent() {
        showToast(getString(R.string.no_scan_permission))
        binding.btnStartDiscover.isEnabled = false
    }

    private fun showToast(text: String, duration: Int = 0) {
        Toast.makeText(this, text, duration).show()
    }
}
