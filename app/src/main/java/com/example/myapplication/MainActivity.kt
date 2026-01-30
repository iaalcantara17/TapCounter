package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var count = 0
    private var tapValue = 1
    private var upgraded = false
    private var goal = 50

    private val prefsName = "tapcounter_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadState()

        setContentView(R.layout.activity_main)

        val tvCount = findViewById<TextView>(R.id.tvCount)
        val tvGoal = findViewById<TextView>(R.id.tvGoal)
        val btnTap = findViewById<Button>(R.id.btnTap)
        val btnUpgrade = findViewById<Button>(R.id.btnUpgrade)
        val btnReset = findViewById<Button>(R.id.btnReset)

        fun refreshUi() {
            tvCount.text = "Taps: $count"
            tvGoal.text = "Goal: $goal"

            btnUpgrade.isEnabled = !upgraded && count >= 100
            btnUpgrade.text = if (upgraded) "x2 Purchased" else "Buy x2 (100 taps)"

            btnTap.text = if (tapValue == 2) "Tap Me (+2)" else "Tap Me (+1)"
        }

        btnTap.setOnClickListener {
            count += tapValue

            if (count >= goal) {
                Toast.makeText(this, "Goal reached: $goal!", Toast.LENGTH_SHORT).show()
                goal *= 2
            }

            refreshUi()
        }

        btnUpgrade.setOnClickListener {
            if (!upgraded && count >= 100) {
                count -= 100
                tapValue = 2
                upgraded = true
                Toast.makeText(this, "Upgrade purchased! Taps now worth 2.", Toast.LENGTH_SHORT).show()
                refreshUi()
            }
        }

        btnReset.setOnClickListener {
            count = 0
            tapValue = 1
            upgraded = false
            goal = 50

            saveState()
            Toast.makeText(this, "Reset complete.", Toast.LENGTH_SHORT).show()
            refreshUi()
        }

        refreshUi()
    }

    override fun onPause() {
        super.onPause()
        saveState()
    }

    private fun saveState() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        prefs.edit()
            .putInt("count", count)
            .putInt("tapValue", tapValue)
            .putBoolean("upgraded", upgraded)
            .putInt("goal", goal)
            .apply()
    }

    private fun loadState() {
        val prefs = getSharedPreferences(prefsName, MODE_PRIVATE)
        count = prefs.getInt("count", 0)
        tapValue = prefs.getInt("tapValue", 1)
        upgraded = prefs.getBoolean("upgraded", false)
        goal = prefs.getInt("goal", 50)
    }
}
