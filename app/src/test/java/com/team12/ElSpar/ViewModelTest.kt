package com.team12.ElSpar

import com.team12.ElSpar.data.DefaultPowerRepository
import com.team12.ElSpar.data.DefaultWeatherRepository
import com.team12.ElSpar.domain.GetPowerPriceUseCase
import com.team12.ElSpar.domain.GetProjectedPowerPriceUseCase
import com.team12.ElSpar.fake.*
import com.team12.ElSpar.model.PriceArea
import com.team12.ElSpar.model.PricePeriod
import com.team12.ElSpar.rules.TestDispatcherRule
import com.team12.ElSpar.ui.viewmodel.MainUiState
import com.team12.ElSpar.ui.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.lang.Thread.sleep


//Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class ViewModelTest {
    //Creating a dispatcher which can be used for all test within this class
    @get:Rule
    val testDispatcher = TestDispatcherRule()

    //Initializing the ViewModel
    private val mainViewModel: MainViewModel by lazy {
        //fake API that gets data from fakeDataSourceList
        val fakeHvaKosterStrommenApiService = FakeHvaKosterStrommenApiService()
        //repos
        val powerRepository = DefaultPowerRepository(fakeHvaKosterStrommenApiService)
        val weatherRepository = DefaultWeatherRepository()

        MainViewModel(
            GetPowerPriceUseCase(powerRepository),
            GetProjectedPowerPriceUseCase(powerRepository, weatherRepository)
        )
    }

    //Checking if uiState updates to Success with correct data after calling getPowerPrice from ViewModel
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun elSparViewModel_getPowerPrice_verifyElSparUiStateSuccess() =
        runTest{
            mainViewModel.getPowerPrice()
            sleep(3000)

            assertEquals(
                MainUiState.Success(PriceArea.NO1, PricePeriod.DAY, FakePowerDataSource.priceDataMapMVA),
                mainViewModel.uiState.value
            )
        }

    //Checking if viewmodels priceArea updates after calling update price area
    @Test
    fun elSparViewModel_updatePriceArea_verifyElSparViewModelCurrentPriceArea() =
        runTest {
            mainViewModel.updatePriceArea(PriceArea.NO2)

            /*30.03 test is not working atm because currentPriceArea is in ViewModel, and not uiState yet
            assertEquals(
                PriceArea.NO2,
                elSparViewModel.uiState.currentPriceArea
            )
             */
        }
}