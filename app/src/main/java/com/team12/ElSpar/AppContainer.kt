package com.team12.ElSpar

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.example.application.Settings
import com.team12.ElSpar.api.DefaultHvaKosterStrommenApiService
import com.team12.ElSpar.api.DefaultMetApiService
import com.team12.ElSpar.api.HvaKosterStrommenApiService
import com.team12.ElSpar.api.MetApiService
import com.team12.ElSpar.data.*
import com.team12.ElSpar.domain.*
import com.team12.ElSpar.network.KtorClient

private const val DATA_STORE_FILE_NAME = "settings.pb"

interface AppContainer {
    val getPowerPriceUseCase: GetPowerPriceUseCase
    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(
    private val context: Context
) : AppContainer {
    private val settingsStore: DataStore<Settings> =
        DataStoreFactory.create(SettingsSerializer) {
            context.dataStoreFile(DATA_STORE_FILE_NAME)
        }

    //API-SERVICES
    private val hvaKosterStrommenApiService: HvaKosterStrommenApiService =
        DefaultHvaKosterStrommenApiService(KtorClient.httpClient)

    private val metApiService : MetApiService =
        DefaultMetApiService(KtorClient.httpClient)

    //REPOSITORIES
    override val settingsRepository: SettingsRepository =
        DefaultSettingsRepository(settingsStore)

    private val powerRepository: PowerRepository =
        DefaultPowerRepository(hvaKosterStrommenApiService)

    private val weatherRepository: WeatherRepository =
        DefaultWeatherRepository(metApiService)

    //DOMAIN LAYER USE CASES
    private val getTemperatureUseCase: GetTemperatureUseCase =
        GetTemperatureUseCase(
            weatherRepository = weatherRepository,
        )

    private val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase =
        GetProjectedPowerPriceUseCase(
            getTemperatureUseCase = getTemperatureUseCase
        )

    override val getPowerPriceUseCase: GetPowerPriceUseCase =
        GetPowerPriceUseCase(
            powerRepository = powerRepository,
            getProjectedPowerPriceUseCase = getProjectedPowerPriceUseCase,
        )
}