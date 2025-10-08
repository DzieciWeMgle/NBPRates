package com.learning.nbprates.domain

import com.learning.nbprates.model.CurrencyRate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
// helper type to facilitate deserialization from response
data class CurrencyWithCurrencyRates(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<CurrencyRate>
)