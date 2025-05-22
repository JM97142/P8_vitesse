package com.example.p8_vitesse

import androidx.lifecycle.SavedStateHandle
import com.example.p8_vitesse.data.api.CurrencyApiService
import com.example.p8_vitesse.data.api.CurrencyResponse
import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.candidateDetails.CandidateDetailActivity
import com.example.p8_vitesse.ui.candidateDetails.CandidateDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class CandidateDetailTest {

    private lateinit var viewModel: CandidateDetailViewModel
    private lateinit var repository: Repository
    private lateinit var currencyApiService: CurrencyApiService
    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    private val fakeCandidate = Items(
        id = 1L,
        firstName = "Alice",
        lastName = "Martin",
        phone = "",
        email = "",
        birthday = "",
        wage = 0.0,
        note = "",
        picture = "",
        favorite = false
    )

    @Before
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        repository = mock(Repository::class.java)
        currencyApiService = mock(CurrencyApiService::class.java)

        savedStateHandle = SavedStateHandle(
            mapOf(CandidateDetailActivity.EXTRA_CANDIDATE_ID to 1L)
        )
        // Mock de la réponse du CurrencyApiService
        `when`(currencyApiService.getEuroRates()).thenReturn(
            CurrencyResponse(
                date = "2023-01-01",
                eur = mapOf("gbp" to 0.85)
            )
        )
        // Stub suspend function dans une coroutine
        `when`(repository.getCandidateById(1L)).thenReturn(fakeCandidate)

        viewModel = CandidateDetailViewModel(
            savedStateHandle,
            repository,
            currencyApiService,
            testDispatcher)
        advanceUntilIdle() // pour que init {} se termine
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `candidate is loaded on init`() = runTest {
        val emitted = mutableListOf<Items?>()

        val job = launch {
            viewModel.candidate.collect {
                emitted.add(it)
                if (it != null) cancel()
            }
        }

        advanceUntilIdle()
        assertEquals(fakeCandidate, emitted.last())
        job.cancel()
    }

    @Test
    fun `toggleFavorite updates favorite state and repository`() = runTest {
        viewModel.toggleFavorite()
        advanceUntilIdle()

        val expected = fakeCandidate.copy(favorite = true)
        assertEquals(expected, viewModel.candidate.value)
        verify(repository).updateCandidate(expected)
    }

    @Test
    fun `refreshCandidate reloads candidate from repository`() = runTest {
        viewModel.refreshCandidate()
        advanceUntilIdle()

        verify(repository, atLeastOnce()).getCandidateById(1L)
    }

    @Test
    fun `deleteCandidate calls repository`() = runTest {
        viewModel.deleteCandidate(1L)
        advanceUntilIdle()

        verify(repository).deleteCandidateById(1L)
    }
}