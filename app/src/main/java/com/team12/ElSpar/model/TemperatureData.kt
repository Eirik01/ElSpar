package com.team12.ElSpar.model

import kotlinx.serialization.Serializable


@Serializable
data class Observation(
    val elementId: String,
    val value: Double,
    val unit: String,
    val timeOffset: String,
    val timeResolution: String
)

@Serializable
data class ObservationData(
    val sourceId: String,
    val referenceTime: String,
    val observations: List<Observation>
)