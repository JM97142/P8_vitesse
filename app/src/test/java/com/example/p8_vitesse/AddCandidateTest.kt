package com.example.p8_vitesse

import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.addCandidate.AddCandidatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class AddCandidateTest {

    private lateinit var viewModel: AddCandidatViewModel
    private lateinit var repository: Repository

    private val testDispatcher = StandardTestDispatcher()

    private val fakeCandidate = Items(
        id = 1L,
        lastName = "Martin",
        firstName = "Alice",
        phone = "",
        email = "",
        birthday = "",
        wage = 0.0,
        note = "",
        picture = "",
        favorite = false
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(Repository::class.java)
        viewModel = AddCandidatViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addCandidate calls repository with converted DTO`() = runTest {
        // Act
        viewModel.addCandidate(fakeCandidate)
        advanceUntilIdle()

        // Assert
        verify(repository).insertCandidate(fakeCandidate.toDto())
    }
}
