package com.team12.ElSpar.domain

import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import java.time.LocalDate
import java.time.LocalDateTime


class GetPowerPriceUseCase (
    private val powerRepository: PowerRepository,
    private val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase
) {
    suspend operator fun invoke(
        period: PricePeriod = PricePeriod.DAY,
        endDate: LocalDate = LocalDate.now(),
        area: PriceArea = PriceArea.NO1
    ): Map<LocalDateTime, Double> {
        val priceData = mutableMapOf<LocalDateTime, Double>()
        val startDate = endDate.minusDays(period.days-1L)
        for (i in 0..period.days-1L) {
            startDate.plusDays(i).let { date ->
                priceData += if (date > LocalDate.now().plusDays(1) ||
                    (date == LocalDate.now().plusDays(1) && LocalDateTime.now().hour < 12)) {
                    getProjectedPowerPriceUseCase(date, area)
                } else powerRepository.getPowerPricesByDate(date, area)
            }
        }
        return priceData.toMap()
    }
}

