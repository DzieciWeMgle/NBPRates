package com.learning.nbprates.domain

import com.learning.nbprates.model.Currency
import com.learning.nbprates.model.Currency.Companion.UNKNOWN_RESULT
import com.learning.nbprates.model.CurrencyRate
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class Repository(
    private val httpClient: HttpClient = HttpClient(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val ioScope = CoroutineScope(ioDispatcher)

    private val dateFormat = DateTimeFormatter.ofPattern(DATE_FORMAT)

    suspend fun getNbpRates(): List<Currency> {
        val deferred = ioScope.async {
            loadList(HTTP_TABLE_A) + loadList(HTTP_TABLE_B)
        }

        return deferred.await()
    }

    suspend fun getNbpRates(
        currencyTable: String,
        currencyCode: String
    ): Pair<Currency, List<CurrencyRate>> {
        val deferred = ioScope.async {
            val date = LocalDate.now()
            val dateFormatted = dateFormat.format(date)
            val olderDate = LocalDate.now().minus( Period.ofDays(DATE_DAYS_AGO))
            val olderDateFormatted = dateFormat.format(olderDate)
            loadCurrency(
                String.format(
                    HTTP_CURRENCY_RATES_TEMPLATE,
                    currencyTable,
                    currencyCode,
                    olderDateFormatted,
                    dateFormatted
                )
            )
        }

        return deferred.await()
    }

    private suspend fun loadList(url: String): List<Currency> {
        var output: List<Currency> = emptyList()
        ioSafe(url) { responseBody ->
            output = Json.decodeFromString<List<Table>>(responseBody).first().let { table ->
                table.rates.map { it.copy(tableCode = table.table) }
            }
        }
        return output
    }

    private suspend fun loadCurrency(url: String): Pair<Currency, List<CurrencyRate>> {
        var output = Pair(UNKNOWN_RESULT, emptyList<CurrencyRate>())
        ioSafe(url) { responseBody ->
            output = Json.decodeFromString<CurrencyWithCurrencyRates>(responseBody).let {
                Pair(
                    Currency(
                        currency = it.currency,
                        code = it.code,
                        mid = it.rates.last().mid,
                        tableCode = it.table
                    ),
                    it.rates,
                )
            }
        }
        return output
    }

    private suspend fun ioSafe(url: String, block: (String)->Unit): Boolean = withContext(ioDispatcher) {
        try {
            val response = httpClient.get(url) {}
            if (response.status == HttpStatusCode.OK) {
                block(response.bodyAsText())
                return@withContext true
            }
        } catch (e: Exception) {
            println("Exception when handling $url: $e")
        }
        false
    }
}

private const val HTTP_BASE = "https://api.nbp.pl/api/exchangerates/"
private const val TABLE_AFFIX = "tables/"
private const val RATES_AFFIX = "rates/"
private const val FORMAT_SUFFIX = "?format=json"
private const val HTTP_CURRENCY_RATES_TEMPLATE = "$HTTP_BASE$RATES_AFFIX%S/%S/%S/%S/$FORMAT_SUFFIX"
private const val HTTP_TABLE_A = "${HTTP_BASE}${TABLE_AFFIX}a$FORMAT_SUFFIX"
private const val HTTP_TABLE_B = "${HTTP_BASE}${TABLE_AFFIX}b$FORMAT_SUFFIX"
private const val DATE_FORMAT = "yyyy-MM-dd"
private const val DATE_DAYS_AGO = 14