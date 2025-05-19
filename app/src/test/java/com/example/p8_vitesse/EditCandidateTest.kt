package com.example.p8_vitesse

import com.example.p8_vitesse.data.repository.Repository
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.editCandidate.EditCandidateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
class EditCandidateViewModelTest {

    private lateinit var viewModel: EditCandidateViewModel
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
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        repository = mock(Repository::class.java)
        viewModel = EditCandidateViewModel(repository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCandidate sets candidate state`() = runTest {
        // Arrange
        `when`(repository.getCandidateById(1L)).thenReturn(fakeCandidate)

        // Act
        viewModel.loadCandidate(1L)
        advanceUntilIdle()

        // Assert
        assertEquals(fakeCandidate, viewModel.candidate.value)
    }

    @Test
    fun `updateCandidate calls repository`() = runTest {
        // Act
        viewModel.updateCandidate(fakeCandidate)
        advanceUntilIdle()

        // Assert
        verify(repository).updateCandidate(fakeCandidate)
    }
}
