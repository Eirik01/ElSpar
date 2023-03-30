package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.data.WeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.rules.TestDispatcherRule
import com.team12.ElSpar.ui.ElSparUiState
import com.team12.ElSpar.ui.ElSparViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep

class ViewModelTest {
    //Creating a dispatcher which can be used for all test within this class
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    //Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
    // 30.3 The test currently fails because mva is treated in powerrepo and not getpowerpriceusecase
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun elSparViewModel_getPowerPrice_verifyElSparUiStateSuccess() =
        runTest{
            //fake API that gets data from fakeDataSourceList
            val fakeHvaKosterStrommenApiService = FakeHvaKosterStrommenApiService()
            //repos
            val powerRepository = DefaultPowerRepository(fakeHvaKosterStrommenApiService)
            val weatherRepository = DefaultWeatherRepository()
            //Usecase
            val getPowerPriceUseCase: GetPowerPriceUseCase = GetPowerPriceUseCase(powerRepository)
            val getProjectedPowerPriceUseCase = GetProjectedPowerPriceUseCase(
                powerRepository,
                weatherRepository)
            //ViewModel
            val elSparViewModel = ElSparViewModel(
                getPowerPriceUseCase,
                getProjectedPowerPriceUseCase
            )

            elSparViewModel.getPowerPrice()
            sleep(3000)

            assertEquals(
                ElSparUiState.Success(FakePowerDataSource.priceDataMap),
                elSparViewModel.uiState.value
            )
        }


}