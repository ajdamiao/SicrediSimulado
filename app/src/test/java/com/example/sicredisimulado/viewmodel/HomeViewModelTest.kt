package com.example.sicredisimulado.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sicredisimulado.data.repository.SicrediRepository
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest : TestCase() {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private val repository = mockk<SicrediRepository>()

    @Test
    fun `when viewModel fetchs data then it should call repository`() {
        val viewModel = instantiateViewModel()

        viewModel.getEvents()

        verify { repository.makeRequest() }

    }

    private fun instantiateViewModel(): HomeViewModel {
        return HomeViewModel()
    }
}