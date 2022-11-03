package dev.achmadk.proasubmission1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentLoginBinding
import dev.achmadk.proasubmission1.models.LoginRequestBody
import dev.achmadk.proasubmission1.ui.login.viewmodels.LoginViewModel
import dev.achmadk.proasubmission1.utils.Resource

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        setupLoginFormHandlers()
        observeViewModels()
    }

    private fun setupLoginFormHandlers() {
        binding.edLoginPassword.setOnEditorActionListener { _, _, _ ->
            login()
            false
        }
        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }
    }

    private fun observeViewModels() {
        viewModel.checkUserDataExist()

        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(R.id.action_LoginFragment_to_StoriesFragment)
            }
        }
        viewModel.loginResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    toggleEnableLoginForm(true)
                }
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    toggleEnableLoginForm(true)
                    val appContext = context?.applicationContext ?: return@observe
                    it.message?.let { message ->
                        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    toggleEnableLoginForm(false)
                }
            }
        }
    }

    fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        val body = LoginRequestBody(email, password)
        viewModel.submitLogin(body)
    }

    private fun toggleEnableLoginForm(value: Boolean) {
        binding.edLoginPassword.isEnabled = value
        binding.edLoginPassword.isEnabled = value
        binding.btnLogin.isEnabled = value
        binding.btnRegister.isEnabled = value
    }
}