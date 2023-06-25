package com.example.teamconsole

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.Manifest.permission.BLUETOOTH_SCAN
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.teamconsole.databinding.ActivityMainBinding
import com.example.teamconsole.infrastructure.BluetoothController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var bluetoothController: BluetoothController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                if (permissions[BLUETOOTH_SCAN] == false || permissions[BLUETOOTH_CONNECT] == false) {
                    showToast(getString(R.string.no_scan_permission))
                    binding.btnStartDiscover.isEnabled = false
                } else {
                    binding.btnStartDiscover.setOnClickListener {
                        bluetoothController.startDiscovery()
                    }
                    lifecycleScope.launch {
                        bluetoothController.boundedDevices.collect {
                            for (i in it!!)
                                Log.i(tag, i.name!!)
                        }
                    }
                }
            }

        requestPermissionLauncher.launch(arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT))
    }

    private fun showToast(text: String, duration: Int = 0) {
        Toast.makeText(this, text, duration).show()
    }
}
