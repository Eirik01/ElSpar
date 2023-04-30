package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.domain.GetTemperatureUseCase
import com.team12.ElSpar.fake.FakeHvaKosterStrommenApiService
import com.team12.ElSpar.fake.FakeMetApiService
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PriceData
import com.team12.ElSpar.model.PricePeriod
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Test
import java.time.LocalDate

class GetPowerPriceUseCaseTest {
    val powerRepository = DefaultPowerRepository(FakeHvaKosterStrommenApiService())
    @Test
    fun getPowerPriceUseCase_invoke_mapIsEmpty() = runTest {
        val result =
            GetPowerPriceUseCase(
                powerRepository,
                GetProjectedPowerPriceUseCase(
                    GetTemperatureUseCase(
                        DefaultWeatherRepository(
                            FakeMetApiService()
                        )
                    )
                )
            ).invoke(endDate = LocalDate.of(3000, 1, 1))

        assertFalse(result.isEmpty())
    }
}