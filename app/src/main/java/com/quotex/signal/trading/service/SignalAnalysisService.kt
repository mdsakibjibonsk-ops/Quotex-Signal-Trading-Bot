package com.quotex.signal.trading.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.quotex.signal.trading.analysis.SignalAnalysisEngine
import com.quotex.signal.trading.models.CandleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class SignalAnalysisService : Service() {
    private val analysisEngine = SignalAnalysisEngine()
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope.launch {
            analyzeMarketData()
        }
        return START_STICKY
    }

    private suspend fun analyzeMarketData() {
        try {
            // সিমুলেটেড রিয়েল মার্কেট ডেটা
            val candles = generateRealisticCandles(100)
            val signal = analysisEngine.analyzeMarket(candles)
            
            Log.d("SignalAnalysis", "Signal: ${signal.signal}, Confidence: ${signal.confidence}%")
            
            // সিগন্যাল পাঠান
            sendBroadcast(Intent("com.quotex.SIGNAL_RECEIVED").apply {
                putExtra("signal", signal.signal)
                putExtra("confidence", signal.confidence)
                putExtra("logicsMatched", signal.logicsMatched)
            })
        } catch (e: Exception) {
            Log.e("SignalAnalysis", "Error: ${e.message}")
        }
        stopSelf()
    }

    private fun generateRealisticCandles(count: Int): List<CandleData> {
        val candles = mutableListOf<CandleData>()
        var basePrice = 100.0
        val timestamp = System.currentTimeMillis()

        repeat(count) { i ->
            val open = basePrice + Random.nextDouble(-2.0, 2.0)
            val close = basePrice + Random.nextDouble(-1.5, 1.5)
            val high = maxOf(open, close) + Random.nextDouble(0.0, 1.0)
            val low = minOf(open, close) - Random.nextDouble(0.0, 1.0)
            val volume = Random.nextLong(1000L, 10000L)

            candles.add(
                CandleData(
                    timestamp = timestamp - ((count - i) * 60000),
                    open = open,
                    high = high,
                    low = low,
                    close = close,
                    volume = volume
                )
            )
            basePrice = close
        }

        return candles
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.launch {
            delay(1000)
        }
    }
}
