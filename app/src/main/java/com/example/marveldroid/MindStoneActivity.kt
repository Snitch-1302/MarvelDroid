package com.example.marveldroid

import android.os.Bundle
import android.util.Base64
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
                val obfuscatedFlag = getObfuscatedFlag()
                val flag = decodeFlag(obfuscatedFlag)
                flagTextView.text = "Flag: $flag"
                MainActivity.updateScore(prefs, "mind", 20, flag)
            } else {
                flagTextView.text = "Exploit failed! Hint: 3 keys, one is 42, hash matters."
            }
        } else {
            flagTextView.text = "Launch me with the right intent!"
        }
    }

    private fun getObfuscatedFlag(): String {
        val baseFlag = "InfinityCTF{mind_stone_hacked}"
        val xorKey = 77
        val xorResult = baseFlag.map { (it.code xor xorKey).toChar() }.joinToString("")
        return Base64.encodeToString(xorResult.toByteArray(), Base64.NO_WRAP)
    }

    private fun decodeFlag(obfuscated: String): String {
        val decoded = Base64.decode(obfuscated, Base64.NO_WRAP)
        val xorKey = 77
        return String(decoded).map { (it.code xor xorKey).toChar() }.joinToString("")
    }

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }
}
