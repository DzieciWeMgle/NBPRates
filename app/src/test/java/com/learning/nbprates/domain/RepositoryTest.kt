package com.learning.nbprates.domain

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    val mockEngine = MockEngine { request ->
        println(request.url.toString())
        when {
            request.url.toString() == TABLE_A_HTTP_REQUEST -> {
                respond(
                    content = TABLE_A,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            request.url.toString() == TABLE_B_HTTP_REQUEST -> {
                respond(
                    content = TABLE_B,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            request.url.toString().contains(CURRENCY_DETAILS_HTTP_REQUEST) -> {
                respond(
                    content = USD_CURRENCY,
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
            }
            else -> throw IOException("Forced Exception")
        }

    }

    lateinit var tested: Repository

    @Before
    fun setup() {
        tested = Repository(HttpClient(mockEngine), testDispatcher)
    }

    @Test
    fun `should return rates list`() = runTest {
        // GIVEN
        // WHEN
        val rates = tested.getNbpRates()

        // THEN
        assertEquals(5, rates.size)
    }

    @Test
    fun `should return currency details`() = runTest {
        // GIVEN
        // WHEN
        val rates = tested.getNbpRates("a", "USD")

        // THEN
        assertEquals("dolar amerykański", rates.component1().currency)
        assertEquals(8, rates.component2().size)
    }

    @Test
    fun `should not crash on exception`() = runTest {
        // GIVEN
        // WHEN
        val rates = tested.getNbpRates("wrong", "unhandled")

        // THEN
        assertEquals(0, rates.component2().size)
    }
}

private const val TABLE_A_HTTP_REQUEST = "https://api.nbp.pl/api/exchangerates/tables/a?format=json"
private const val TABLE_B_HTTP_REQUEST = "https://api.nbp.pl/api/exchangerates/tables/b?format=json"
private const val CURRENCY_DETAILS_HTTP_REQUEST = "https://api.nbp.pl/api/exchangerates/rates/A/USD/"

private const val TABLE_A = "[{\"table\":\"A\",\"no\":\"194/A/NBP/2025\",\"effectiveDate\":\"2025-10-07\",\"rates\":[{\"currency\":\"yuan renminbi (Chiny)\",\"code\":\"CNY\",\"mid\":0.5089},{\"currency\":\"SDR (MFW)\",\"code\":\"XDR\",\"mid\":4.9781}]}]"
private const val TABLE_B = "[{\"table\":\"B\",\"no\":\"039/B/NBP/2025\",\"effectiveDate\":\"2025-10-01\",\"rates\":[{\"currency\":\"vatu (Vanuatu)\",\"code\":\"VUV\",\"mid\":0.030019},{\"currency\":\"wymienialna marka (Bośnia i Hercegowina)\",\"code\":\"BAM\",\"mid\":2.1717},{\"currency\":\"złoto Zimbabwe\",\"code\":\"ZWG\",\"mid\":0.1338}]}]"
private const val USD_CURRENCY = "{\"table\":\"A\",\"currency\":\"dolar amerykański\",\"code\":\"USD\",\"rates\":[{\"no\":\"186/A/NBP/2025\",\"effectiveDate\":\"2025-09-25\",\"mid\":3.6295},{\"no\":\"187/A/NBP/2025\",\"effectiveDate\":\"2025-09-26\",\"mid\":3.6528},{\"no\":\"188/A/NBP/2025\",\"effectiveDate\":\"2025-09-29\",\"mid\":3.6411},{\"no\":\"189/A/NBP/2025\",\"effectiveDate\":\"2025-09-30\",\"mid\":3.6315},{\"no\":\"190/A/NBP/2025\",\"effectiveDate\":\"2025-10-01\",\"mid\":3.6245},{\"no\":\"191/A/NBP/2025\",\"effectiveDate\":\"2025-10-02\",\"mid\":3.6210},{\"no\":\"192/A/NBP/2025\",\"effectiveDate\":\"2025-10-03\",\"mid\":3.6253},{\"no\":\"193/A/NBP/2025\",\"effectiveDate\":\"2025-10-06\",\"mid\":3.6446}]}"