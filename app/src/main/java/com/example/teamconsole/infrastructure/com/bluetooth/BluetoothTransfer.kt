package com.example.teamconsole.infrastructure.com.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import com.example.teamconsole.infrastructure.SerialCommunication
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class BluetoothTransfer(private val socket: BluetoothSocket) : SerialCommunication {

    private val tag = "BluetoothTransfer"
    private val obj = ""
    private val inStream: InputStream = socket.inputStream
    private val outStream: OutputStream = socket.outputStream
    private var buffer: ByteArray = ByteArray(1024)

    override fun sendMessage(bytes: Array<Byte>): Array<Byte> {
        synchronized(obj) {
            if (!socket.isConnected) {
                Log.i(tag, "Socket not connected")
                throw IOException("Socket not connected")
            }
            write(bytes)
            try {
                buffer = ByteArray(1024)
                buffer[0] = inStream.read().toByte()
                buffer[1] = inStream.read().toByte()
                buffer[2] = inStream.read().toByte()
                buffer[3] = inStream.read().toByte()
                val dataSize = Integer.toHexString(buffer[3].toInt()).toInt()
                for (i in 0 until dataSize) buffer[4 + i] = inStream.read().toByte()
                buffer = buffer.copyOf(4 + dataSize)
                Log.i(tag, "Bytes read: " + toString(buffer, buffer.size))
            } catch (e: IOException) {
                Log.d(tag, "Input stream was disconnected", e)
            }
            return buffer.toTypedArray();
        }
    }

    override fun cancel() {
        try {
            socket.close();
        } catch (e: IOException) {
            Log.e(tag, "Error occurred when cancel", e)
        }
    }

    private fun write(bytes: Array<Byte>) {
        try {
            val lBytes = bytes.toByteArray()
            outStream.write(lBytes)
            Log.i(tag, "Bytes sent: " + toString(lBytes, lBytes.size))
        } catch (e: IOException) {
            Log.e(tag, "Error occurred when write data", e)
        }
    }

    private fun toString(bytes: ByteArray, length: Int): String {
        val sb = StringBuilder()
        sb.append('[')
        for (i in 0 until length) {
            sb.append(String.format("%02X", bytes[i]))
            sb.append(',')
        }
        sb.deleteCharAt(sb.length - 1)
        sb.append(']')
        return sb.toString()
    }
}
