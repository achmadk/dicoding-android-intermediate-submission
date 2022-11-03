package dev.achmadk.proasubmission1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentRegisterBinding
import dev.achmadk.proasubmission1.models.RegisterRequestBody
import dev.achmadk.proasubmission1.ui.register.viewmodels.RegisterViewModel
import dev.achmadk.proasubmission1.utils.Resource

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private val viewModel: RegisterViewModel by viewModels()

    private var _binding: FragmentRegisterBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        setupRegisterFormHandlers()
        observeViewModels()
    }

    private fun setupRegisterFormHandlers() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun observeViewModels() {
        val appContext = context?.applicationContext ?: return
        viewModel.registerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.loading.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
                    }
                    val bundle = bundleOf()
                    bundle.putBoolean("from_register", true)
                    findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment, bundle)
                }
                is Resource.Error -> {
                    binding.loading.visibility = View.GONE
                    it.message?.let { message ->
                        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                }
            }
        }
    }

    fun register() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        val body = RegisterRequestBody(name, email, password)
        viewModel.submitRegister(body)
    }
}