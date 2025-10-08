package com.learning.nbprates.ui.rateslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.learning.nbprates.domain.Repository
import com.learning.nbprates.NbpRatesApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyRatesViewModel(
    application: Application
): AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<CurrencyRatesListUiState>(CurrencyRatesListUiState(emptyList()))
    val uiState: StateFlow<CurrencyRatesListUiState> = _uiState

    private val repository: Repository = getApplication<NbpRatesApplication>().repository
    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val currencies = repository.getNbpRates()
            _uiState.value = CurrencyRatesListUiState(currencies.sortedBy { currency -> currency.currency })
        }
    }
}