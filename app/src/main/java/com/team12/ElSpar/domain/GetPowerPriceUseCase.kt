package com.team12.ElSpar.domain

import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PriceArea
import java.time.Duration.between
import java.time.LocalDateTime

class GetPowerPriceUseCase (
    private val powerRepository: PowerRepository,
) {
    suspend operator fun invoke(
        startTime: LocalDateTime = LocalDateTime.now(),
        endTime: LocalDateTime = LocalDateTime.now(),
        priceArea: PriceArea = PriceArea.NO1
    ): Map<LocalDateTime, Double> {
        val priceData = mutableMapOf<LocalDateTime, Double>()
        for (i in 0..between(startTime, endTime).toDays()) {
            priceData += powerRepository.getPowerPricesByDate(startTime.plusDays(i), priceArea)
        }
        return priceData.toMap()
    }
}

