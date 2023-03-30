package com.team12.ElSpar.fake

import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.model.PriceArea
import java.time.LocalDateTime

//may not need class
class FakePowerRepository: PowerRepository {
    override suspend fun getPowerPricesByDate(
        date: LocalDateTime,
        area: PriceArea
    ): Map<LocalDateTime, Double>
    {
        return FakePowerDataSource.priceDataMap
    }
}