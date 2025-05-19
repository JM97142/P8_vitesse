package com.example.p8_vitesse

import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.home.candidatesFragments.AllCandidatesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class GetAllCandidatesTest {

    private lateinit var viewModel: AllCandidatesViewModel
    private lateinit var repository: Repository
    private val testDispatcher = StandardTestDispatcher()

    // Candidats simulés
    private val fakeCandidates = listOf(
        Items(
            1L,
            "Alice",
            "Martin",
            "",
            "",
            "",
            0.0,
            "",
            "",
        false),
        Items(
            2L,
            "Bob",
            "Durand",
            "",
            "",
            "",
            0.0,
            "",
            "",
            false),
        Items(
            3L,
            "Claire",
            "Dubois",
            "",
            "",
            "",
            0.0,
            "",
            "",
            false)
    )
    // Flow simulé retourné par le repository
    private val candidatesFlow = MutableStateFlow(fakeCandidates)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(Repository::class.java)
        `when`(repository.fetchAllCandidatesFlow()).thenReturn(candidatesFlow)
        viewModel = AllCandidatesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `filtered returns all candidates when query is blank`() = runTest {
        val emitted = mutableListOf<List<Items>>()
        val job = launch {
            viewModel.filtered.collect {
                emitted.add(it)
                if (it.isNotEmpty()) cancel() // on sort quand on a une valeur utile
            }
        }

        advanceUntilIdle()

        assertEquals(fakeCandidates, emitted.last())
        job.cancel()
    }

    @Test
    fun `filtered returns filtered candidates when query is set`() = runTest {
        val emitted = mutableListOf<List<Items>>()
        val job = launch {
            viewModel.filtered.collect {
                emitted.add(it)
                if (it.any { item -> item.lastName.equals("Bob", ignoreCase = true) }) cancel()
            }
        }

        viewModel.setQuery("bob")
        advanceUntilIdle()

        val filtered = emitted.last()
        assertEquals(1, filtered.size)
        assertEquals("Bob", filtered.first().lastName)

        job.cancel()
    }
}