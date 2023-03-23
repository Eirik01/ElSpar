package com.team12.ElSpar.domain

import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.data.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class GetProjectedPowerPriceUseCase(
    powerRepository: PowerRepository,
    weatherRepository: WeatherRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    suspend operator fun invoke(endTime: LocalDateTime) =
        withContext(defaultDispatcher) {

        }
}