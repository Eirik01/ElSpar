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
import com.team12.ElSpar.rules.TestDispatcherRule
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.io.File


//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class FakeAppContainer(
    val iODispatcher: TestDispatcher = StandardTestDispatcher(),
    val settingsRepository: SettingsRepository
    //val settingsStore: DataStore<Settings>
) {
    /*
    val datastore = mock(DataStore::class.java)
    //val fakeFile = mock(File::class.java)
    val context: Context = mock(Context::class.java)
    val file: File = File.createTempFile("test_prefix", "test_suffix")
    private val DATA_STORE_FILE_NAME = "my_data_store"

    private val settingsStore: DataStore<Settings> =
        DataStoreFactory.create(SettingsSerializer) {
            context.dataStoreFile("file")

        }

     */


    //fake API that gets data from fake datasource
    private val hvaKosterStrommenApiService: HvaKosterStrommenApiService =
        FakeHvaKosterStrommenApiService()

    //fake API that gets data from fake datasource
    private val metApiService : MetApiService =
        FakeMetApiService()
    /*
    val settingsRepository: SettingsRepository =
        DefaultSettingsRepository(settingsStore)

     */




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

/*

class MockSettingsRepository {
    val mockSettingsRepository = mock(SettingsRepository::class.java)

    fun create() {
            whenever(mockSettingsRepository.settingsFlow).thenReturn(flowOf(Settings.getDefaultInstance()))
            doAnswer { invocation ->
                mockSettingsRepository.updatePriceArea(invocation.getArgument(0))
                null
            }.whenever(mockSettingsRepository).updatePriceArea(any(Settings.PriceArea::class.java))
            doAnswer { invocation ->
                val activity = invocation.getArgument(0) as Settings.Activity
                val value = invocation.getArgument(1) as Int
                mockSettingsRepository.updateActivity(activity, value)
                null
            }.whenever(mockSettingsRepository)
                .updateActivity(any(Settings.Activity::class.java), anyInt())
            doAnswer { invocation ->
                mockSettingsRepository.initializeValues()
                null
            }.whenever(mockSettingsRepository).initializeValues()
    }
}

 */

