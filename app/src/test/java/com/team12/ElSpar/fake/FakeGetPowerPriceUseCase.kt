package com.team12.ElSpar.fake

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.model.PriceArea
import java.time.Duration
import java.time.LocalDateTime


//may not need this class

class FakeGetPowerPriceUseCase(
    private val fakePowerRepository: DefaultPowerRepository
) {
    suspend operator fun invoke(
        startTime: LocalDateTime = LocalDateTime.now(),
        endTime: LocalDateTime = LocalDateTime.now(),
        priceArea: PriceArea = PriceArea.NO1
    ): Map<LocalDateTime, Double> {
        val priceData = mutableMapOf<LocalDateTime, Double>()
        for (i in 0..Duration.between(startTime, endTime).toDays()) {
            priceData += fakePowerRepository.getPowerPricesByDate(startTime.plusDays(i), priceArea)
        }
        return priceData.toMap()
    }
}