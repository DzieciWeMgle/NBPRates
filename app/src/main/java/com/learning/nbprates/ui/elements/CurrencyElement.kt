package com.learning.nbprates.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.learning.nbprates.model.Currency

@Preview
@Composable
fun CurrencyElement(
    currency: Currency = Currency("Test currency", "TST", 1.23),
    evenIndex: Boolean = false,
    onClick: () -> Unit = { /*Empty for preview*/ },
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(onClick = { onClick() })
            .background(if (evenIndex) { Color.LightGray} else { Color.Unspecified }),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Start) {
            Text(currency.code)
            Spacer(Modifier.width(4.dp))
            Text(currency.currency)
        }
        Text(currency.mid.toString())
    }
}