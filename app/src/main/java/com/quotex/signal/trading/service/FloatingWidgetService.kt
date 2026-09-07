package com.quotex.signal.trading.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.quotex.signal.trading.R

class FloatingWidgetService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingView: FrameLayout
    private var isExpanded = false
    private var lastX = 0f
    private var lastY = 0f

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        createFloatingWidget()
    }

    private fun createFloatingWidget() {
        val inflater = LayoutInflater.from(this)
        floatingView = inflater.inflate(R.layout.floating_widget, null) as FrameLayout

        val params = WindowManager.LayoutParams().apply {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            format = PixelFormat.TRANSLUCENT
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            width = 100
            height = 100
            gravity = Gravity.TOP or Gravity.START
            x = 0
            y = 100
        }

        windowManager.addView(floatingView, params)

        val scanButton = floatingView.findViewById<ImageButton>(R.id.scan_button)
        val statusText = floatingView.findViewById<TextView>(R.id.status_text)

        scanButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val deltaX = event.rawX - lastX
                    val deltaY = event.rawY - lastY

                    val params2 = floatingView.layoutParams as WindowManager.LayoutParams
                    params2.x += deltaX.toInt()
                    params2.y += deltaY.toInt()

                    windowManager.updateViewLayout(floatingView, params2)
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_UP -> {
                    if (event.eventTime - event.downTime < 200) {
                        startSignalAnalysis(statusText)
                    }
                }
            }
            false
        }
    }

    private fun startSignalAnalysis(statusText: TextView) {
        statusText.text = "স্ক্যান করছে..."
        Intent(this, SignalAnalysisService::class.java).apply {
            startService(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::floatingView in this::class.java.declaredFields) {
            windowManager.removeView(floatingView)
        }
    }
}
