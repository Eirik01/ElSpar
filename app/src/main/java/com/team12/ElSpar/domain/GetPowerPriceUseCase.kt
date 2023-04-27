package com.team12.ElSpar.domain

import com.example.application.Settings.PriceArea
import com.team12.ElSpar.api.PriceNotAvailableException
import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.network.NoConnectionException
import java.nio.channels.UnresolvedAddressException
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
        return priceData.toMap()
    }
}

