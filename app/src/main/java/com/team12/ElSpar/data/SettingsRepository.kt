package com.team12.ElSpar.data

import androidx.datastore.core.DataStore
import com.team12.ElSpar.Settings
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

//DEFAULT VALUES
private const val SHOWER = 10
private const val WASH = 60
private const val OVEN = 15
private const val CAR = 24

interface SettingsRepository {
    val settingsFlow: Flow<Settings>
    suspend fun initialStartupCompleted()
    suspend fun updatePriceArea(area: Settings.PriceArea)
    suspend fun updateActivity(activity: Settings.Activity, value: Int)
    suspend fun initializeValues()
}

class DefaultSettingsRepository(
    private val settingsStore: DataStore<Settings>
) : SettingsRepository {
    override val settingsFlow: Flow<Settings> = settingsStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(Settings.getDefaultInstance())
            } else {
                throw exception
            }
        }

    override suspend fun initialStartupCompleted() {
        settingsStore.updateData { settings ->
            settings.toBuilder().setInitialStartupCompleted(true).build()
        }
    }

    override suspend fun updatePriceArea(area: Settings.PriceArea) {
        settingsStore.updateData { settings ->
            settings.toBuilder().setArea(area).build()
        }
    }

    override suspend fun updateActivity(activity: Settings.Activity, value: Int) {
        settingsStore.updateData { settings ->
            when (activity) {
                Settings.Activity.SHOWER -> settings.toBuilder().setShower(value).build()
                Settings.Activity.WASH -> settings.toBuilder().setWash(value).build()
                Settings.Activity.OVEN -> settings.toBuilder().setOven(value).build()
                Settings.Activity.CAR -> settings.toBuilder().setCar(value).build()
                Settings.Activity.UNRECOGNIZED -> settings
            }
        }
    }

    override suspend fun initializeValues() {
        settingsStore.updateData { settings ->
            settings.toBuilder()
                .setShower(SHOWER)
                .setWash(WASH)
                .setOven(OVEN)
                .setCar(CAR)
                .build()
        }
    }
}