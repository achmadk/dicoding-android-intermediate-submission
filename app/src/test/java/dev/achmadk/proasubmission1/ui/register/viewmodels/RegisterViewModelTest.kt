package dev.achmadk.proasubmission1.ui.register.viewmodels

import androidx.lifecycle.Observer
import dev.achmadk.proasubmission1.fakes.repositories.RegisterRepositoryFake
import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.models.RegisterResponseBody
import dev.achmadk.proasubmission1.utils.BaseViewModelTest
import dev.achmadk.proasubmission1.utils.ContextFake
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RegisterViewModelTest: BaseViewModelTest() {
    private lateinit var registerRepository: RegisterRepositoryFake
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerRepository = RegisterRepositoryFake()
        registerViewModel = RegisterViewModel(registerRepository, ContextFake)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should register successfully`() = runTest {
        val observer = Observer<Resource<RegisterResponseBody>> {}
        try {
            val actualRegisterResponse = registerViewModel.registerResponse.observeForever(observer)

            val body = RegisterRequestBody(
                name = "Achmad Kurnianto",
                email = "achmad.kurnianto@gmail.com",
                password = "testHello12345"
            )
            registerViewModel.submitRegister(body)

            advanceUntilIdle()

            assertEquals(registerViewModel.registerResponse.value!!.javaClass, Resource.Success::class.java)

            assertNotNull(actualRegisterResponse)
        } finally {
            registerViewModel.registerResponse.removeObserver(observer)
        }
    }
}