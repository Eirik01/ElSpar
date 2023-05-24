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
    ): Map<LocalDateTime, Double> = withContext(iODispatcher) {
        val priceData = mutableMapOf<LocalDateTime, Double>()
        val startDate = endDate.minusDays(period.days-1L)
        for (i in 0 until period.days) {

            //for each date from start date:
            startDate.plusDays(i.toLong()).let { date ->

                //check if its a future date after tomorrows date OR
                //  date is set to exactly tomorrow and clock has passed 13:00 today:
                priceData += if (date > LocalDate.now().plusDays(1)
                    || (date == LocalDate.now().plusDays(1) && LocalDateTime.now().hour < 13)) {
                    //true: call the projected powerprice usecase
                    getProjectedPowerPriceUseCase(date, area)
                } else try {
                    //false: call the powerPricesByDate usecase
                    powerRepository.getPowerPricesByDate(date, area)
                } catch (e: PriceNotAvailableException) {
                    //exception if price is not available, and call projected price use case
                    getProjectedPowerPriceUseCase(date, area)
                } catch (e: NoConnectionException) {
                    throw e
                }
            }
        }
        priceData.toMap()
    }
}

