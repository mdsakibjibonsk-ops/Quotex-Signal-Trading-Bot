package com.quotex.signal.trading.models

data class CandleData(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

data class SignalData(
    val signal: String, // "UP" or "DOWN"
    val confidence: Double, // 0-100%
    val logicsMatched: Int, // কত লজিক ম্যাচ করেছে
    val totalLogics: Int, // মোট ১০০টি লজিক
    val timestamp: Long,
    val details: String
)

data class TradingConfig(
    val minConfidence: Double = 90.0, // মিনিমাম ৯০% কনফিডেন্স
    val candleTime: Int = 1, // ১ মিনিটের ট্রেড
    val riskPercentage: Double = 2.0,
    val stopLoss: Double = 2.0,
    val takeProfit: Double = 3.0
)
