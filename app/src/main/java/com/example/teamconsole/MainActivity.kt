package com.example.teamconsole

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.teamconsole.databinding.ActivityMainBinding
import com.example.teamconsole.infrastructure.BluetoothController
import com.example.teamconsole.infrastructure.SimpleBluetoothController
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothController: BluetoothController = SimpleBluetoothController(this)

        binding.btnStartDiscover.setOnClickListener {
            bluetoothController.startDiscovery()
        }

        lifecycleScope.launch {
            bluetoothController.boundedDevices.collect() {
                for (i in it!!)
                    Log.i(tag, i.name!!)
            }
        }

    }
}
