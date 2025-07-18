package com.example.marveldroid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.system.exitProcess

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
                resultTextView.text = "Secret ritual unlocked! 🖤\nFlag: InfinityCTF{hidden_truth}"
            } else {
                val name = nameInput.text.toString().trim()
                if (validateName(name)) {
                    resultTextView.text = "Soul Stone rejects your offer! ❌"
                } else {
                    resultTextView.text = "Invalid sacrifice. The stone remains untouched. ❌"
                }
            }
        }
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