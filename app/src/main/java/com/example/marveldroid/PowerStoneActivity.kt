package com.example.marveldroid

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream

class PowerStoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_power_stone)

        val prefs = getSharedPreferences("CTF_Score", MODE_PRIVATE)
        val editText = findViewById<EditText>(R.id.inputKey)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        submitButton.setOnClickListener {
            val enteredKey = editText.text.toString()
            val compressedEncodedKey = getString(R.string.power_stone_secret)

            val decodedKey = try {
                // Step 1: Base64 decode (Gzipped Data)
                val compressedData = Base64.decode(compressedEncodedKey, Base64.NO_WRAP)

                // Step 2: Gzip Decompress
                val decompressedBytes = gunzip(compressedData)

                // Step 3: Base64 Decode to get the original flag
                String(Base64.decode(decompressedBytes, Base64.NO_WRAP), Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }

            if (decodedKey != null && enteredKey == decodedKey) {
                resultText.text = "Flag: $decodedKey"
                resultText.textSize = 18f
                resultText.setTypeface(null, android.graphics.Typeface.BOLD)
                MainActivity.updateScore(prefs, "power", 10, decodedKey)
            } else {
                resultText.text = "Wrong key! Try again."
                resultText.textSize = 18f
                resultText.setTypeface(null, android.graphics.Typeface.BOLD)
            }
        }
    }

    private fun gunzip(compressed: ByteArray): ByteArray {
        return try {
            val inputStream = GZIPInputStream(ByteArrayInputStream(compressed))
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            outputStream.toByteArray()
        } catch (e: Exception) {
            ByteArray(0) // Return empty byte array in case of an error
        }
    }
}
