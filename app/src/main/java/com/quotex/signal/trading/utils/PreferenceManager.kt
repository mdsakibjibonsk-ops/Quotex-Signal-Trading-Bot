package com.quotex.signal.trading.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val preferences: SharedPreferences = 
        context.getSharedPreferences("quotex_prefs", Context.MODE_PRIVATE)

    fun setMinConfidence(value: Double) {
        preferences.edit().putFloat("min_confidence", value.toFloat()).apply()
    }

    fun getMinConfidence(): Double = preferences.getFloat("min_confidence", 90f).toDouble()

    fun setTradeDuration(minutes: Int) {
        preferences.edit().putInt("trade_duration", minutes).apply()
    }

    fun getTradeDuration(): Int = preferences.getInt("trade_duration", 1)

    fun setWidgetEnabled(enabled: Boolean) {
        preferences.edit().putBoolean("widget_enabled", enabled).apply()
    }

    fun isWidgetEnabled(): Boolean = preferences.getBoolean("widget_enabled", false)
}
