package com.team12.ElSpar.fake

import androidx.datastore.core.DataStore
import com.team12.ElSpar.Settings
import com.team12.ElSpar.data.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import org.mockito.Mockito.mock

/*
class FakeSettingsRepository(
    val settingsStore: DataStore<FakeSettings>,
    override val iODispatcher: CoroutineDispatcher
) : SettingsRepository {
    val settings = Settings.PriceArea.NO1
    override val settingsFlow: Flow<Settings> = flow{
        emit(FakeSettings)
    }


        /*settingsStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit()
            } else {
                throw exception
            }
        }

         */
    override suspend fun initialStartupCompleted() {
        TODO("Not yet implemented")
    }

    override suspend fun updatePriceArea(area: Settings.PriceArea) {
        TODO("Not yet implemented")
    }

    override suspend fun updateActivity(activity: Settings.Activity, value: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun initializeValues() {
        TODO("Not yet implemented")
    }
}


class FakeSettingsRepository(
    override val iODispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : SettingsRepository {
    private var _settingsFlow = MutableStateFlow(mock(Settings::class.java))

    override val settingsFlow: Flow<Settings>
        get() = _settingsFlow

    fun setSettings(settings: Settings) {
        _settingsFlow.value = settings
    }
    override suspend fun initialStartupCompleted() {
        TODO("Not yet implemented")
    }

    override suspend fun updatePriceArea(area: Settings.PriceArea) {
        TODO("Not yet implemented")
    }

    override suspend fun updateActivity(activity: Settings.Activity, value: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun initializeValues() {
        TODO("Not yet implemented")
    }

    // other implementations of SettingsRepository interface methods
}


 */