package com.learning.nbprates.ui.ratedetails

import com.learning.nbprates.model.Currency
import com.learning.nbprates.model.CurrencyRate

data class CurrencyRateDetailsUiState(val currencyCode: String, val currencyDetails: Currency?, val currencyRates: List<CurrencyRate>)