package com.example.marveldroid

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SpaceStoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_stone)

        val etFlagInput = findViewById<EditText>(R.id.etFlagInput)
        val btnSubmitFlag = findViewById<Button>(R.id.btnSubmitFlag)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // Call NetworkLeak to send the flag over the network
        val dynamicFlag = NetworkLeak.sendFlag() // This is the flag that's sent over the network

        btnSubmitFlag.setOnClickListener {
            val enteredFlag = etFlagInput.text.toString().trim()

            // Check if the entered flag is correct (use the dynamically generated flag)
            if (enteredFlag == dynamicFlag) {
                tvResult.text = "Correct Flag! Well done!"
                tvResult.setTextColor(getColor(android.R.color.holo_green_dark))
            } else {
                tvResult.text = "Wrong Flag! Try again."
                tvResult.setTextColor(getColor(android.R.color.holo_red_dark))
            }
        }
    }
}
