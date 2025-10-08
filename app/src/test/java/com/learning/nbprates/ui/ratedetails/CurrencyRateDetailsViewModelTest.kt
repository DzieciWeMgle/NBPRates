package com.learning.nbprates.ui.ratedetails

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.learning.nbprates.NbpRatesApplication
import com.learning.nbprates.TestCoroutineRule
import com.learning.nbprates.domain.Repository
import com.learning.nbprates.model.Currency
import com.learning.nbprates.model.CurrencyRate
import com.learning.nbprates.model.Destination
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRateDetailsViewModelTest {

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    val mockRepository: Repository = mockk {
        coEvery { getNbpRates(any(), any()) } returns CURRENCY_AND_RATES
    }

    val mockkApplication: NbpRatesApplication = mockk {
        every { repository } returns mockRepository
    }

    lateinit var mockSavedStateHandle: SavedStateHandle
    lateinit var tested: CurrencyRateDetailsViewModel

    @Before
    fun setup() {
        mockSavedStateHandle = mockk(relaxed = true)
        mockSavedStateHandle.mockkToRoute(
            Destination.CurrencyRateDetails(
                TABLE,
                CURRENCY_CODE
            )
        )
    }

    @Test
    fun `should call repository for rates on creation`() = runTest {
        // GIVEN
        tested = CurrencyRateDetailsViewModel(mockkApplication, mockSavedStateHandle)
        // WHEN
        advanceUntilIdle()

        // THEN
        coVerify { mockRepository.getNbpRates(TABLE, CURRENCY_CODE) }
    }

    @Test
    fun `should post rates into uiState`() = runTest {
        // GIVEN
        tested = CurrencyRateDetailsViewModel(mockkApplication, mockSavedStateHandle)
        advanceUntilIdle()

        // WHEN

        val uiState = tested.uiState.value

        // THEN
        assertEquals(CURRENCY_AND_RATES.first, uiState.currencyDetails)
        // rates are sorted already
        assertEquals(
            CURRENCY_AND_RATES.second.sortedByDescending { it.effectiveDate },
            uiState.currencyRates
        )
    }

    private inline fun <reified T : Any> SavedStateHandle.mockkToRoute(page: T) {
        mockkStatic("androidx.navigation.SavedStateHandleKt")
        every { toRoute<T>() } returns page
    }

}

private const val TABLE = "A"
private const val CURRENCY_CODE = "tst"
private val CURRENCY_AND_RATES = Pair(
    Currency("test", "tst", 1.23, "A"),
    second = listOf(
        CurrencyRate(
            no = "1",
            effectiveDate = "1234-45-67",
            mid = 1.23
        ),
        CurrencyRate(
            no = "2",
            effectiveDate = "1234-45-68",
            mid = 1.25
        )
    )
)