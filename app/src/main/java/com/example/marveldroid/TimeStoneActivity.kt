package com.example.marveldroid

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimeStoneActivity : AppCompatActivity() {

    // FIX: previously a hardcoded constant string, so the same flag would
    // be valid forever. Now generated once per activity launch, matching
    // the dynamic-flag pattern used elsewhere in this app.
    private lateinit var flag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_stone)

        val prefs = getSharedPreferences("CTF_Score", MODE_PRIVATE)
        val inputMessage = findViewById<EditText>(R.id.inputMessage)
        val encryptButton = findViewById<Button>(R.id.encryptButton)
        val outputText = findViewById<TextView>(R.id.outputText)

        flag = generateDynamicFlag("time")
        val preEncryptedFlag = toughEncrypt(flag)
        outputText.text = "Decrypt this: $preEncryptedFlag (Hint: Study the encryption process)"

        encryptButton.setOnClickListener {
            val message = inputMessage.text.toString()
            if (message == flag) { // User enters the correct flag directly
                outputText.text = "Correct! Flag: $flag"
                MainActivity.updateScore(prefs, "time", 20, flag)
            } else {
                val encryptedMessage = toughEncrypt(message)
                outputText.text = "Encrypted: $encryptedMessage (Hint: Flag is pre-encrypted above)"
            }
        }
    }

    private fun generateDynamicFlag(prefix: String): String {
        val timestampSuffix = System.currentTimeMillis().toString().takeLast(6)
        return "InfinityCTF{${prefix}_$timestampSuffix}"
    }

    private fun toughEncrypt(input: String): String {
        // Step 1: XOR with key 42
        val xorResult = input.map { it.code xor 42 }.joinToString("") { it.toChar().toString() }
        // Step 2: Reverse the string
        val reversed = xorResult.reversed()
        // Step 3: Base64 encode
        val base64Encoded = Base64.encodeToString(reversed.toByteArray(), Base64.NO_WRAP)
        // Step 4: Custom shift (rotate each char by 3)
        return base64Encoded.map { (it.code + 3).toChar() }.joinToString("")
    }

    // Optional: Provide a decryption helper for testing (not shown to users)
    private fun toughDecrypt(input: String): String {
        val unshifted = input.map { (it.code - 3).toChar() }.joinToString("")
        val base64Decoded = Base64.decode(unshifted, Base64.NO_WRAP)
        val unreversed = String(base64Decoded).reversed()
        return unreversed.map { it.code xor 42 }.joinToString("") { it.toChar().toString() }
    }
}
