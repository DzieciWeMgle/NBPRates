package com.learning.nbprates

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learning.nbprates.model.Destination
import com.learning.nbprates.ui.ratedetails.CurrencyRateDetails
import com.learning.nbprates.ui.rateslist.CurrencyRatesList
import com.learning.nbprates.ui.theme.NBPRatesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NBPRatesTheme {
                NavigableScreens()
            }
        }
    }
}

@Composable
// navigation graph definition
fun NavigableScreens(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destination.CurrencyRatesList,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        composable<Destination.CurrencyRatesList> {
            CurrencyRatesList(onCurrencySelected = { currency ->
                navController.navigate(
                    route = Destination.CurrencyRateDetails(
                        tableCode = currency.tableCode, currencyCode = currency.code
                    )
                )
            })
        }
        composable<Destination.CurrencyRateDetails> {
            CurrencyRateDetails(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
