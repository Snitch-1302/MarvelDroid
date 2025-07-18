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

        //resultTextView = findViewById(R.id.resultTextView)
       // checkButton = findViewById(R.id.checkButton)

        checkButton.setOnClickListener {
            if (securityCheck()) {
                val encryptedFlag = byteArrayOf(0x53, 0x3A, 0x5D, 0x11, 0x42, 0x72, 0x35)
                val flag = decryptFlag(encryptedFlag, 0x23)
                resultTextView.text = "Reality is yours! 🎭\nFlag: $flag"
            } else {
                resultTextView.text = "Reality remains unbent. ❌"
            }
        }
    }

    private fun securityCheck(): Boolean {
        return false
    }

    private fun decryptFlag(data: ByteArray, key: Int): String {
        return data.map { (it.toInt() xor key).toChar() }.joinToString("")
    }
}