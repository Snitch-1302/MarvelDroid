package com.example.marveldroid

import android.util.Log
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import kotlin.concurrent.thread

object NetworkLeak {
    // Method to send the dynamic flag over the network
    fun sendFlag(): String {
        val dynamicFlag = "InfinityCTF{${UUID.randomUUID().toString().substring(0, 8)}}"
        Log.d("NetworkLeak", "Leaking flag: $dynamicFlag")

        // Run network operations in a background thread
        thread {
            // Sending the flag to a dummy endpoint
            val url = URL("https://eonpdxpb2zf5esp.m.pipedream.net")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "text/plain")
                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.write(dynamicFlag)
                outputStream.flush()
                outputStream.close()

                val responseCode = connection.responseCode
                Log.d("NetworkLeak", "Server Response Code: $responseCode")
            } catch (e: Exception) {
                Log.e("NetworkLeak", "Request failed", e)
            } finally {
                connection.disconnect()
            }
        }

        return dynamicFlag
    }
}