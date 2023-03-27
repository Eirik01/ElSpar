package com.team12.ElSpar.api

import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PriceData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import java.time.LocalDateTime

interface HvaKosterStrommenApiService {
    suspend fun getPowerPricesByDate(date: LocalDateTime, area: PriceArea): List<PriceData>
}

class DefaultHvaKosterStrommenApiService(
    private val client: HttpClient,
    private val baseURL: String = "https://www.hvakosterstrommen.no/api/v1/prices"
) : HvaKosterStrommenApiService{
    override suspend fun getPowerPricesByDate(date: LocalDateTime, area: PriceArea): List<PriceData> {
        return client.get(baseURL +
                "/" + date.year +
                "/" + date.monthValue.toString().padStart(2, '0') +
                "-" + date.dayOfMonth.toString().padStart(2, '0') +
                "_" + area.name + ".json").body()
    }
}