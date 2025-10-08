package com.learning.nbprates.ui.rateslist

import com.learning.nbprates.NbpRatesApplication
import com.learning.nbprates.TestCoroutineRule
import com.learning.nbprates.domain.Repository
import com.learning.nbprates.model.Currency
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyRatesViewModelTest {

    @get:Rule
    var mainCoroutineRule = TestCoroutineRule()

    val mockRepository: Repository = mockk {
        coEvery { getNbpRates() } returnsMany listOf(emptyList(), CURRENCIES)
    }

    val mockkApplication: NbpRatesApplication = mockk {
        every { repository } returns mockRepository
    }

    lateinit var tested: CurrencyRatesViewModel

    @Test
    fun `should call repository for currencies on creation()`() = runTest {
        // GIVEN
        tested = CurrencyRatesViewModel(mockkApplication)
        // WHEN
        advanceUntilIdle()

        // THEN
        coVerify { mockRepository.getNbpRates() }
    }

    @Test
    fun `should get empty list initially`() = runTest {
        // GIVEN
        tested = CurrencyRatesViewModel(mockkApplication)
        advanceUntilIdle()

        // WHEN
        val uiState = tested.uiState.value

        // THEN
        assertTrue(uiState.currencies.isEmpty())
    }

    @Test
    fun `should get non-empty list on refresh`() = runTest {
        // GIVEN
        tested = CurrencyRatesViewModel(mockkApplication)
        advanceUntilIdle()

        // WHEN
        tested.refresh()
        advanceUntilIdle()
        val uiState = tested.uiState.value

        // THEN
        assertTrue(uiState.currencies.isNotEmpty())
    }
}

private val CURRENCIES = listOf<Currency>(
    Currency("test one", "tst1", 1.23, "A"),
    Currency("test two", "tst2", 4.56, "A"),
)