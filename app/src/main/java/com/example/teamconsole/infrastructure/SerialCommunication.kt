package com.example.teamconsole.infrastructure

interface SerialCommunication {
   fun sendMessage(byte: Array<Byte>) : Array<Byte>
   fun cancel()
}