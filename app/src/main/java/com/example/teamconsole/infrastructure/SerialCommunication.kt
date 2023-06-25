package com.example.teamconsole.infrastructure

interface SerialCommunication {
   fun sendMessage(bytes: Array<Byte>) : Array<Byte>
   fun cancel()
}