package com.example.teamconsole.presentation

import com.example.teamconsole.infrastructure.models.SppDevice

data class SppDevicesUIState(
    private val foundSppDevices: List<SppDevice> = emptyList(),
    private val boundSppDevices: List<SppDevice> = emptyList()
) {

}