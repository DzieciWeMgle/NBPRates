package com.learning.nbprates.ui.ratedetails

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.learning.nbprates.NbpRatesApplication
import com.learning.nbprates.domain.Repository
import com.learning.nbprates.model.Destination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyRateDetailsViewModel(application: Application, savedStateHandle: SavedStateHandle) :
    AndroidViewModel(application) {

    private val routeDetails = savedStateHandle.toRoute<Destination.CurrencyRateDetails>()
    private val repository: Repository = getApplication<NbpRatesApplication>().repository

    private val _uiState: MutableStateFlow<CurrencyRateDetailsUiState> = MutableStateFlow(
        CurrencyRateDetailsUiState(routeDetails.currencyCode, null, emptyList())
    )
    val uiState: StateFlow<CurrencyRateDetailsUiState> = _uiState

    init {
        viewModelScope.launch {
            val (currencyDetails, currencyRates) = repository.getNbpRates(
                routeDetails.tableCode,
                routeDetails.currencyCode
            )
            _uiState.value = CurrencyRateDetailsUiState(
                currencyCode = routeDetails.currencyCode,
                currencyDetails = currencyDetails,
                currencyRates = currencyRates.sortedByDescending { currencyRate -> currencyRate.effectiveDate },
            )
        }
    }
}