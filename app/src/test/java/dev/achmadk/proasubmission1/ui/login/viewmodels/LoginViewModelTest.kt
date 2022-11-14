package dev.achmadk.proasubmission1.ui.login.viewmodels

import androidx.lifecycle.Observer
import dev.achmadk.proasubmission1.data.repositories.IUserPreferenceRepository
import dev.achmadk.proasubmission1.fakes.repositories.LoginRepositoryFake
import dev.achmadk.proasubmission1.fakes.repositories.UserPreferenceRepositoryFake
import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.models.LoginResponseBody
import dev.achmadk.proasubmission1.ui.login.repositories.ILoginRepository
import dev.achmadk.proasubmission1.utils.BaseViewModelTest
import dev.achmadk.proasubmission1.utils.ContextFake
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginViewModelTest: BaseViewModelTest() {
    private lateinit var iLoginRepository: ILoginRepository
    private lateinit var iUserPreferenceRepository: IUserPreferenceRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        iLoginRepository = LoginRepositoryFake()
        iUserPreferenceRepository = UserPreferenceRepositoryFake()
        loginViewModel = LoginViewModel(iLoginRepository, iUserPreferenceRepository, ContextFake)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should login successfully`() = runTest {
        val observer = Observer<Resource<LoginResponseBody>> {}
        try {
            val actualLoginResponse = loginViewModel.loginResponse.observeForever(observer)

            val body = LoginRequestBody(
                email = "achmad.kurnianto@gmail.com",
                password = "testHello12345"
            )
            loginViewModel.submitLogin(body)
            advanceUntilIdle()

            assertEquals(loginViewModel.loginResponse.value!!.javaClass, Resource.Success::class.java)
            assertEquals(loginViewModel.isLoggedIn.value, true)

            assertNotNull(actualLoginResponse)
        } finally {
            loginViewModel.loginResponse.removeObserver(observer)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `isLoggedIn value should be false when user not login`() = runTest {
        val observer = Observer<Boolean> {}
        try {
            val actualLoginResponse = loginViewModel.isLoggedIn.observeForever(observer)

            assertEquals(loginViewModel.isLoggedIn.value, false)

            assertNotNull(actualLoginResponse)
        } finally {
            loginViewModel.isLoggedIn.removeObserver(observer)
        }
    }
}