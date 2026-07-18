package com.example.marveldroid

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RealityStoneActivity : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var checkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reality_stone)

        // BUG FIX: these two lines were commented out in the original code,
        // but checkButton.setOnClickListener was still called right below --
        // since both are `lateinit var`, that would throw an
        // UninitializedPropertyAccessException the instant this activity
        // loaded, crashing it immediately rather than showing the intended
        // "Reality Intact" screen.
        resultTextView = findViewById(R.id.resultTextView)
        checkButton = findViewById(R.id.checkButton)

        checkButton.setOnClickListener {
            if (securityCheck()) {
                // BUG FIX: the original byte array here didn't actually
                // decode to a real flag -- XORing it with the key produced
                // 7 garbage characters, not "InfinityCTF{reality_hacked}"
                // as intended. This is the corrected byte array, verified
                // to round-trip properly: encrypt("InfinityCTF{reality_hacked}",
                // key=0x23) -> these bytes -> decrypt(...) gives back the
                // original flag exactly.
                val encryptedFlag = byteArrayOf(
                    0x6A, 0x4D, 0x45, 0x4A, 0x4D, 0x4A, 0x57, 0x5A, 0x60, 0x77,
                    0x65, 0x58, 0x51, 0x46, 0x42, 0x4F, 0x4A, 0x57, 0x5A, 0x7C,
                    0x4B, 0x42, 0x40, 0x48, 0x46, 0x47, 0x5E
                )
                val flag = decryptFlag(encryptedFlag, 0x23)
                resultTextView.text = "Reality is yours! \uD83C\uDFAD\nFlag: $flag"
            } else {
                resultTextView.text = "Reality remains unbent. \u274C"
            }
        }
    }

    // Intentionally always returns false -- this is the actual challenge,
    // not a bug. The player is meant to patch this method (via smali edit
    // + APK rebuild/resign, or a runtime hook like Frida) to return true,
    // exactly as documented in the walkthrough.
    private fun securityCheck(): Boolean {
        return false
    }

    private fun decryptFlag(data: ByteArray, key: Int): String {
        return data.map { (it.toInt() and 0xFF xor key).toChar() }.joinToString("")
    }
}
