package com.learning.nbprates.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.learning.nbprates.model.CurrencyRate
import kotlin.math.abs

@Composable
fun CurrencyRateElement(
    currencyRate: CurrencyRate,
    currentCurrencyRateMid: Double,
    evenIndex: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                if (evenIndex) {
                    Color.LightGray
                } else {
                    Color.Unspecified
                }
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(currencyRate.effectiveDate)
        val rateColor: Color =
            if (abs(currentCurrencyRateMid - currencyRate.mid) > RATE_DIFFERENCE_MULTIPLIER_FOR_INDICATION * currentCurrencyRateMid) {
                Color.Red
            } else {
                Color.Unspecified
            }
        Text(currencyRate.mid.toString(), color = rateColor)
    }

}

private const val RATE_DIFFERENCE_MULTIPLIER_FOR_INDICATION = 0.1