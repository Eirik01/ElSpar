package com.team12.ElSpar.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class Observation(
    val air_temperature: Double,
    val wind_speed: Double,
    val time : String
)

data class ObservationData(
    val timeseries: List<Observation>
)