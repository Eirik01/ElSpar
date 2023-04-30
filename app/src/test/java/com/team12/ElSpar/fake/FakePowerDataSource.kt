package com.team12.ElSpar.fake

import com.team12.ElSpar.model.PriceData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month


object FakePowerDataSource {
    val priceData1 = PriceData(
        NOK_per_kWh = 10 * 0.10,
        EUR_per_kWh = 0.10,
        EXR = 10.0,
        time_start = "2023-01-30T00:00:00+02:00",
        time_end = "2023-01-30T01:00:00+02:00"
    )
    val priceData2 = PriceData(
        NOK_per_kWh = 10 * 0.20,
        EUR_per_kWh = 0.20,
        EXR = 10.0,
        time_start = "2023-01-30T01:00:00+02:00",
        time_end = "2023-01-30T02:00:00+02:00"
    )
    val priceDataList = listOf<PriceData>(
        priceData1,
        priceData2
    )
    val priceDataMap = mapOf<LocalDateTime, Double>(
        LocalDateTime.of(2023, 1, 30, 0, 0) to  priceData1.NOK_per_kWh*125,
        LocalDateTime.of(2023, 1, 30, 1, 0) to  priceData2.NOK_per_kWh*125,
    )
    val priceDataMapMVA = mapOf<LocalDateTime, Double>(
        LocalDateTime.of(2023, 1, 30, 0, 0) to  priceData1.NOK_per_kWh*125,
        LocalDateTime.of(2023, 1, 30, 1, 0) to  priceData2.NOK_per_kWh*125,
    )
}