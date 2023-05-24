package com.team12.ElSpar.fake

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.team12.ElSpar.Settings
import com.team12.ElSpar.api.HvaKosterStrommenApiService
import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.data.*
import com.team12.ElSpar.domain.*
import android.content.Context
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import java.io.File



class FakeAppContainer(
    val iODispatcher: TestDispatcher = StandardTestDispatcher(),
    val settingsRepository: SettingsRepository
) {
    //fake API that gets data from fake datasource
    private val hvaKosterStrommenApiService: HvaKosterStrommenApiService =
        FakeHvaKosterStrommenApiService()

    //fake API that gets data from fake datasource
    private val metApiService : MetApiService =
        FakeMetApiService()

    val powerRepository: PowerRepository =
        DefaultPowerRepository(hvaKosterStrommenApiService)

    val weatherRepository: WeatherRepository =
        DefaultWeatherRepository(metApiService)

    val getTemperatureUseCase: GetTemperatureUseCase =
        GetTemperatureUseCase(
            weatherRepository = weatherRepository,
        )

    private val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase =
        GetProjectedPowerPriceUseCase(
            getTemperatureUseCase = getTemperatureUseCase
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val getPowerPriceUseCase: GetPowerPriceUseCase =
        GetPowerPriceUseCase(
            powerRepository = powerRepository,
            getProjectedPowerPriceUseCase = getProjectedPowerPriceUseCase,
            iODispatcher = iODispatcher
        )
}
