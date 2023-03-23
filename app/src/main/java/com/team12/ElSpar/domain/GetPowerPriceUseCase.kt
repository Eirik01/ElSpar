package com.team12.ElSpar.domain

import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PriceArea
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class GetPowerPriceUseCase (
    private val powerRepository: PowerRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(
        startTime: LocalDateTime = LocalDateTime.now(),
        endTime: LocalDateTime = LocalDateTime.now(),
        priceArea: PriceArea = PriceArea.NO1): Map<LocalDateTime, Double> =
        withContext(defaultDispatcher) {
            mapOf(LocalDateTime.of(
                2023,
                3,
                23,
                0,
                0,
                0
            ) to 0.69);
    }
}

