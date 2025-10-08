package com.learning.nbprates.ui.ratedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
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
import com.learning.nbprates.ui.elements.CurrencyRateElement

@Preview
@Composable
fun CurrencyRateDetails(
    onBack: () -> Unit = {},
    viewModel: CurrencyRateDetailsViewModel = viewModel<CurrencyRateDetailsViewModel>(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentCurrencyRateMid = uiState.currencyRates.firstOrNull()?.mid ?: 0.0
    Column(Modifier.padding(4.dp)) {
        Text(
            text = stringResource(R.string.ratedetails_title, uiState.currencyCode),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(4.dp))
        if (uiState.currencyDetails != null && uiState.currencyDetails != Currency.UNKNOWN_RESULT) {
            uiState.currencyDetails?.let { CurrencyElement(currency = it, false) }
            Spacer(Modifier.height(8.dp))
            LazyColumn() {
                itemsIndexed(uiState.currencyRates) { index, currency ->
                    CurrencyRateElement(currency, currentCurrencyRateMid, index.isEven())
                }
            }
        } else {
            Text(stringResource(R.string.ratedetails_label_no_details))
        }
        Spacer(Modifier.height(8.dp).weight(1f))
        Button(onClick = { onBack() }) { Text("Back") }
    }
}