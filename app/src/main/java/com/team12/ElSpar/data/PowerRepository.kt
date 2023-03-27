package com.team12.ElSpar.data

import com.team12.ElSpar.api.HvaKosterStrommenApiService
import com.team12.ElSpar.model.PriceArea
import java.time.LocalDateTime

interface PowerRepository {
    suspend fun getPowerPricesByDate(
        date: LocalDateTime,
        area: PriceArea
    ): Map<LocalDateTime, Double>
}

class DefaultPowerRepository(
    private val hvaKosterStrommenApiService: HvaKosterStrommenApiService
) : PowerRepository {
    override suspend fun getPowerPricesByDate(
        date: LocalDateTime,
        area: PriceArea
    ): Map<LocalDateTime, Double> {
        val priceData = mutableMapOf<LocalDateTime, Double>()
        hvaKosterStrommenApiService
            .getPowerPricesByDate(date, area)
            .forEach {
                priceData.put(
                    LocalDateTime.parse(it.time_start.dropLast(6)),
                    it.NOK_per_kWh
                )
            }
        return priceData.toMap()
    }
}