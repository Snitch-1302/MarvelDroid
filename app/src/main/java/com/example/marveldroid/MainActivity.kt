package com.example.marveldroid

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("CTF_Score", MODE_PRIVATE)
        scoreTextView = findViewById(R.id.scoreTextView)

        val stoneChallenges = mapOf(
            R.id.btn_power_stone to PowerStoneActivity::class.java,
            R.id.btn_space_stone to SpaceStoneActivity::class.java,
            R.id.btn_reality_stone to RealityStoneActivity::class.java,
            R.id.btn_soul_stone to SoulStoneActivity::class.java,
            R.id.btn_time_stone to TimeStoneActivity::class.java,
            R.id.btn_mind_stone to MindStoneActivity::class.java
        )

        stoneChallenges.forEach { (buttonId, activityClass) ->
            findViewById<Button>(buttonId).setOnClickListener {
                startActivity(Intent(this, activityClass))
            }
        }

        updateScoreDisplay()
    }

    override fun onResume() {
        super.onResume()
        updateScoreDisplay() // Refresh score when returning to main screen
    }

    private fun updateScoreDisplay() {
        val totalScore = prefs.getInt("total_score", 0)
        scoreTextView.text = "Score: $totalScore / 100"
    }

    companion object {
        fun updateScore(prefs: SharedPreferences, stone: String, points: Int, flag: String) {
            val editor = prefs.edit()
            // Construct the key explicitly (e.g., "power_solved")
            val solvedKey = "${stone}_solved"
            // Only award points if the flag hasn't been solved yet
            if (!prefs.getBoolean(solvedKey, false)) {
                editor.putBoolean(solvedKey, true)
                val currentScore = prefs.getInt("total_score", 0)
                editor.putInt("total_score", currentScore + points)
                editor.apply()
            }
        }
    }
}