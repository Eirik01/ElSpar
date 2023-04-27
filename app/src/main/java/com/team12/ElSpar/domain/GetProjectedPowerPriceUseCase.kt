package com.team12.ElSpar.domain


import com.team12.ElSpar.Settings.PriceArea
import java.time.LocalDate
import java.time.LocalDateTime

class GetProjectedPowerPriceUseCase(
    getTemperatureUseCase: GetTemperatureUseCase
) {
    operator fun invoke(date: LocalDate, area: PriceArea): Map<LocalDateTime, Double> {
        val projectedPriceData = mutableMapOf<LocalDateTime, Double>()
        for (h in 0..23) {
            projectedPriceData[date.atStartOfDay().plusHours(h.toLong())] = 0.0
        }
        return projectedPriceData.toMap()
    }

}