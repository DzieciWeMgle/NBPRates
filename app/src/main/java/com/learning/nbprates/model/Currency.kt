package com.learning.nbprates.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class Currency(val currency: String, val code: String, val mid: Double, val tableCode: String = "") {
    companion object {
        val UNKNOWN_RESULT = Currency("n/a", "n/a", 0.0)
    }
}