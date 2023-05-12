package com.team12.ElSpar.domain

import com.team12.ElSpar.Settings.PriceArea
import com.team12.ElSpar.exceptions.PriceNotAvailableException
import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.exceptions.NoConnectionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime

class GetPowerPriceUseCase (
    private val powerRepository: PowerRepository,
    private val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase,
    private val iODispatcher: CoroutineDispatcher = Dispatchers.IO // testing

) {
    suspend operator fun invoke(
        period: PricePeriod,
        endDate: LocalDate,
        area: PriceArea,
    ): Map<LocalDateTime, Double> = withContext(iODispatcher){
        val priceData = mutableMapOf<LocalDateTime, Double>()
        val startDate = endDate.minusDays(period.days-1L)
        for (i in 0 until period.days) {
            startDate.plusDays(i.toLong()).let { date ->
                priceData += if (date > LocalDate.now().plusDays(1)
                    || (date == LocalDate.now().plusDays(1) && LocalDateTime.now().hour < 13)) {
                    getProjectedPowerPriceUseCase(date, area)
                } else try {
                    powerRepository.getPowerPricesByDate(date, area)
                } catch (e: PriceNotAvailableException) {
                    getProjectedPowerPriceUseCase(date, area)
                } catch (e: NoConnectionException) {
                    throw e
                }
            }
        }
        priceData.toMap()
    }
}

