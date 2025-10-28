package com.learning.nbprates.ui.rateslist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.learning.nbprates.R
import com.learning.nbprates.ktx.isEven
import com.learning.nbprates.model.Currency
import com.learning.nbprates.ui.elements.CurrencyElement

@Preview
@Composable
fun CurrencyRatesList(
    onCurrencySelected: (Currency) -> Unit = { /*Empty For Preview*/ },
    viewModel: CurrencyRatesViewModel = viewModel<CurrencyRatesViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(Modifier.padding(4.dp)) {
        Text(
            text = stringResource(R.string.rateslist_title),
            style = MaterialTheme.typography.titleMedium
        )
        if (uiState.isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }
        Spacer(Modifier.height(8.dp))
        uiState.currencies.let {
            if (it.isNotEmpty()) {
                ActualCurrencyRatesList(uiState.currencies) { onCurrencySelected(it) }
            } else {
                Spacer(Modifier.height(8.dp).weight(1f))
                NoItemsFound(isLoading = uiState.isLoading, { viewModel.refresh() })
            }
        }
    }
}

@Composable
fun ActualCurrencyRatesList(currencies: List<Currency>, onCurrencySelected: (Currency) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemsIndexed(currencies) { index, currency ->
            CurrencyElement(currency, index.isEven()) { onCurrencySelected(currency) }
            Spacer(Modifier.height(2.dp))
        }
    }
}

@Composable
fun NoItemsFound( isLoading: Boolean, onRefresh: () -> Unit) {
    Button(onClick = { onRefresh() }, enabled = !isLoading) {
        Text(
            text = stringResource(R.string.rateslist_button_no_items),
        )
    }
}