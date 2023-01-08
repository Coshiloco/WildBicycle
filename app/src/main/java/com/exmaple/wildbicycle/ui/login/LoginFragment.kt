package com.exmaple.wildbicycle.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exmaple.wildbicycle.BuildConfig
import com.exmaple.wildbicycle.databinding.FragmentLoginBinding
import com.exmaple.wildbicycle.managers.SHA512.SHA512Hash
import com.exmaple.wildbicycle.utils.UserNotFoundEmailException
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (BuildConfig.DEBUG) {
            binding.fragmentLoginEmail.editText?.setText("pablohurtadohg@gmail.com")
            binding.fragmentLoginPassword.editText?.setText("papa2023")
        }

        setListeners()
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.tryLoginExistingUser()
    }

    private fun setListeners() = with(binding) {
        fragmentLoginIniciarSesion.setOnClickListener {
            viewModel.login(
                fragmentLoginEmail.editText?.text.toString(),
                fragmentLoginPassword.editText?.text.toString()
            )
        }
        fragmentLoginRegistrarse.setOnClickListener {
            viewModel.registerEmailUser(
                fragmentLoginEmail.editText?.text.toString(),
                fragmentLoginPassword.editText?.text.toString()
            )
        }
        fragmentLoginCambiarPassword.setOnClickListener {
            viewModel.resetPassword(
                fragmentLoginEmail.editText?.text.toString(),
                fragmentLoginPassword.editText?.text.toString()
            )
        }
    }

    private fun setObservers() = with(viewModel) {
        navigate.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventoLogin ->
                when (eventoLogin) {
                    LoginViewModel.Navigate.Home -> LoginFragmentDirections.actionNavLoginToHomeFragment()
                        .let {
                            findNavController().navigate(it)
                        }

                    LoginViewModel.Navigate.GoBack -> Toast.makeText(
                        requireContext(),
                        "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        resetPassword.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventoResetPassword ->
                when (eventoResetPassword) {
                    LoginViewModel.OptionsResetPassword.SendEmail -> Toast.makeText(
                        requireContext(),
                        "El email sera enviado a la cuenta asociada con este contraseña para cambiarla",
                        Toast.LENGTH_LONG
                    ).show()
                    LoginViewModel.OptionsResetPassword.UserNotFoundEmail -> Toast.makeText(
                        requireContext(),
                        UserNotFoundEmailException().message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        errorMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { mensajeError ->
                Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
