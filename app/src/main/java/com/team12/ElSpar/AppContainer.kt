package com.team12.ElSpar

import com.team12.ElSpar.api.DefaultHvaKosterStrommenApiService
import com.team12.ElSpar.api.HvaKosterStrommenApiService
import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.PowerRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.network.KtorClient

interface AppContainer {
    val getPowerPriceUseCase: GetPowerPriceUseCase
    val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase
}

class DefaultAppContainer : AppContainer {
    private val hvaKosterStrommenApiService: HvaKosterStrommenApiService =
        DefaultHvaKosterStrommenApiService(KtorClient.httpClient)

    private val powerRepository: PowerRepository =
        DefaultPowerRepository(hvaKosterStrommenApiService)
    private val weatherRepository = DefaultWeatherRepository()

    override val getPowerPriceUseCase: GetPowerPriceUseCase =
        GetPowerPriceUseCase(
            powerRepository = powerRepository
        )

    override val getProjectedPowerPriceUseCase: GetProjectedPowerPriceUseCase =
        GetProjectedPowerPriceUseCase(
            powerRepository = powerRepository,
            weatherRepository = weatherRepository
        )
}