package com.example.marveldroid

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SoulStoneActivity : AppCompatActivity() {
    private lateinit var nameInput: EditText
    private lateinit var sacrificeButton: Button
    private lateinit var resultTextView: TextView
    private var tapCount = 0
    private var tapStartTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soul_stone)

        nameInput = findViewById(R.id.nameInput)
        sacrificeButton = findViewById(R.id.sacrificeButton)
        resultTextView = findViewById(R.id.resultTextView)

        sacrificeButton.setOnClickListener {
            if (isBackdoorActivated()) {
                // FIX: previously a hardcoded constant string
                // ("InfinityCTF{hidden_truth}"), so the same flag would be
                // valid forever. Now generated dynamically at the moment
                // the backdoor triggers, matching the dynamic-flag pattern
                // used elsewhere in this app.
                val flag = generateDynamicFlag("soul")
                resultTextView.text = "Secret ritual unlocked! \uD83D\uDDA4\nFlag: $flag"
            } else {
                val name = nameInput.text.toString().trim()
                if (validateName(name)) {
                    resultTextView.text = "Soul Stone rejects your offer! \u274C"
                } else {
                    resultTextView.text = "Invalid sacrifice. The stone remains untouched. \u274C"
                }
            }
        }
    }

    private fun generateDynamicFlag(prefix: String): String {
        val timestampSuffix = System.currentTimeMillis().toString().takeLast(6)
        return "InfinityCTF{${prefix}_$timestampSuffix}"
    }

    private fun validateName(name: String): Boolean {
        return name.lowercase() == "gamora" || name.lowercase() == "thanos"
    }

    private fun isBackdoorActivated(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (tapStartTime == 0L || currentTime - tapStartTime > 3000) {
            tapStartTime = currentTime
            tapCount = 1
        } else {
            tapCount++
        }
        return tapCount >= 5
    }
}
