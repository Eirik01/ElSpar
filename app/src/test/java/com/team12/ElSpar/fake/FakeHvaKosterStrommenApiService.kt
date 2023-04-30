package com.team12.ElSpar.fake

import com.team12.ElSpar.api.HvaKosterStrommenApiService
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PriceData
import java.time.LocalDate
import java.time.LocalDateTime

class FakeHvaKosterStrommenApiService: HvaKosterStrommenApiService {
    override suspend fun getPowerPricesByDate(date: LocalDate, area: PriceArea): List<PriceData> {
        return FakePowerDataSource.priceDataList
    }
}