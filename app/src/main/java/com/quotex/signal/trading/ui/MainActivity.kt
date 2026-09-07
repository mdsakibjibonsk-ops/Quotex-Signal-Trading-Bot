package com.quotex.signal.trading.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quotex.signal.trading.R
import com.quotex.signal.trading.service.FloatingWidgetService

class MainActivity : AppCompatActivity() {
    private lateinit var signalReceiver: BroadcastReceiver
    private lateinit var statusView: TextView
    private var floatingWidgetActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusView = findViewById(R.id.status_view)
        val enableWidgetBtn = findViewById<Button>(R.id.enable_widget_btn)
        val settingsBtn = findViewById<Button>(R.id.settings_btn)
        val exitBtn = findViewById<Button>(R.id.exit_btn)

        enableWidgetBtn.setOnClickListener { enableFloatingWidget() }
        settingsBtn.setOnClickListener { startSettings() }
        exitBtn.setOnClickListener { finish() }

        setupBroadcastReceiver()
        checkAndRequestPermissions()
    }

    private fun enableFloatingWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivityForResult(intent, 1)
                return
            }
        }

        if (!floatingWidgetActive) {
            startService(Intent(this, FloatingWidgetService::class.java))
            floatingWidgetActive = true
            Toast.makeText(this, "ফ্লোটিং উইজেট সক্ষম", Toast.LENGTH_SHORT).show()
            statusView.text = "ফ্লোটিং উইজেট: সক্রিয়"
        }
    }

    private fun setupBroadcastReceiver() {
        signalReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val signal = intent.getStringExtra("signal") ?: "WAIT"
                val confidence = intent.getDoubleExtra("confidence", 0.0)
                val logicsMatched = intent.getIntExtra("logicsMatched", 0)

                statusView.text = "সিগন্যাল: $signal\nআত্মবিশ্বাস: ${String.format("%.1f", confidence)}%\nলজিক: $logicsMatched/100"
            }
        }

        registerReceiver(signalReceiver, IntentFilter("com.quotex.SIGNAL_RECEIVED"))
    }

    private fun startSettings() {
        startActivity(Intent(this, SettingsActivity::class.java))
    }

    private fun checkAndRequestPermissions() {
        // পারমিশন চেক করুন
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(signalReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
