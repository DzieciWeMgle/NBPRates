package com.learning.nbprates.model

import kotlinx.serialization.Serializable
@Serializable
sealed class Destination {
    @Serializable
    object CurrencyRatesList: Destination()

    @Serializable
    data class CurrencyRateDetails(val tableCode: String, val currencyCode: String): Destination() {

    }
}