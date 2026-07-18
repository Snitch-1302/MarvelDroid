package com.example.marveldroid

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MindStoneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind_stone)

        val prefs = getSharedPreferences("CTF_Score", MODE_PRIVATE)
        val flagTextView = findViewById<TextView>(R.id.flagTextView)

        if (intent.action == "com.example.marveldroid.EXPLOIT") {
            val key1 = intent.getStringExtra("key1") ?: ""
            val key2 = intent.getStringExtra("key2") ?: ""
            val key3 = intent.getIntExtra("key3", 0)
            val combined = "$key1:$key2:$key3"
            val hash = md5(combined)

            if (hash == "667ee1c89b2de33513b0a84c57860c7e" && key3 == 42) { // Correct MD5 for "mind:stone:42"
                // FIX: the original version obfuscated and immediately
                // de-obfuscated a hardcoded string within this same class,
                // which made the "flag" trivial (always
                // "InfinityCTF{mind_stone_hacked}" regardless of when the
                // exploit ran). This now generates a genuinely dynamic
                // flag, tied to the moment the exploit succeeds, matching
                // the same "6-digit timestamp suffix" pattern used
                // elsewhere in this app (Space Stone, Time Stone, Soul
                // Stone) -- so a screenshot from one run can't be replayed
                // as a valid flag on another run.
                val flag = generateDynamicFlag("mind")
                flagTextView.text = "Flag: $flag"
                MainActivity.updateScore(prefs, "mind", 20, flag)
            } else {
                flagTextView.text = "Exploit failed! Hint: 3 keys, one is 42, hash matters."
            }
        } else {
            flagTextView.text = "Launch me with the right intent!"
        }
    }

    private fun generateDynamicFlag(prefix: String): String {
        val timestampSuffix = System.currentTimeMillis().toString().takeLast(6)
        return "InfinityCTF{${prefix}_$timestampSuffix}"
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
