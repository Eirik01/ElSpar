package com.team12.ElSpar
package com.team12.ElSpar

import com.team12.ElSpar.api.DefaultMetApiService
import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.domain.GetTemperatureUseCase
import com.team12.ElSpar.fake.FakeHvaKosterStrommenApiService
import com.team12.ElSpar.fake.FakeMetApiService
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.rules.MainCoroutineRule
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class GetPowerPriceUseCaseTest {

    val powerRepository = DefaultPowerRepository(FakeHvaKosterStrommenApiService())
    val getPowerPriceUseCase: GetPowerPriceUseCase =
        GetPowerPriceUseCase(
            powerRepository = powerRepository,
            getProjectedPowerPriceUseCase = GetProjectedPowerPriceUseCase(
                GetTemperatureUseCase(
                    DefaultWeatherRepository(
                        FakeMetApiService()
                    )
                )
            )
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getPowerPriceUseCase_invoke_mapIsNotEmpty() =
        runTest {
            val result =
                getPowerPriceUseCase(
                    period = PricePeriod.DAY,
                    endDate = LocalDate.of(2023, 1, 1),
                    area = Settings.PriceArea.NO1
                )

            assertTrue(result.isNotEmpty())
        }
    @Test
    fun getPowerPriceUseCase_invoke_containsDataForWeek() =
        runTest {
            val result =
                getPowerPriceUseCase(
                    period = PricePeriod.WEEK,
                    endDate = LocalDate.of(2023, 1, 7),
                    area = Settings.PriceArea.NO1
                )
            assertTrue(
                result.size == 7
            )
        }
}