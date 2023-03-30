package com.team12.ElSpar.model

enum class PricePeriod(val days: Int, val text: String) {
    DAY(1, "1 dag"),
    WEEK(7, "7 dager"),
    MONTH(30, "30 dager"),
}