package com.exmaple.wildbicycle.ui.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exmaple.wildbicycle.BuildConfig
import com.exmaple.wildbicycle.R
import com.exmaple.wildbicycle.bases.BaseViewModel
import com.exmaple.wildbicycle.databinding.FragmentLoginBinding
import com.exmaple.wildbicycle.utils.UserNotFoundEmailException
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val signInRequest by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(WEB_CLIENT_ID)
            .requestEmail()
            .build()
    }

    private val googleRegisterResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.checkResultAndExecute {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                viewModel.googleLogin(account) { userRegisteredBBDD ->
                    userRegisteredBBDD.fold(
                        onSuccess = {
                            if (it) Toast.makeText(
                                requireContext(),
                                "Se ha deslogeado con Google",
                                Toast.LENGTH_LONG
                            ).show()
                            else Toast.makeText(
                                requireContext(),
                                "No se ha deslogeado con Google",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = {

                        }
                    )
                }
            }.onFailure { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    private inline fun ActivityResult.checkResultAndExecute(block: ActivityResult.() -> Unit) =
        if (resultCode == Activity.RESULT_OK) runCatching(block)
        else Result.failure(Exception("Something went wrong"))

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
            binding.fragmentLoginEmail.editText?.setText("a@a.com")
            binding.fragmentLoginPassword.editText?.setText("123456")
        }

        setListeners()
        setObservers()
    }

    override fun onStart() {
        super.onStart()
        viewModel.tryLoginExistingUser()
    }

    private fun setListeners() = with(binding) {
        fragmentLoginEmail.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.validateEmail(
                fragmentLoginEmail.editText?.text.toString()
            )
        }

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

        fragmentLoginIniciarSesionGoogle.setOnClickListener {
            googleRegisterResult.launch(
                GoogleSignIn.getClient(
                    requireActivity(),
                    signInRequest
                ).signInIntent
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
            it.getContentIfNotHandled()?.let { eventLogin ->
                when (eventLogin) {
                    BaseViewModel.Navigate.Home -> LoginFragmentDirections.actionNavLoginToHomeFragment()
                        .let { action ->
                            findNavController().navigate(action)
                        }

                    else -> Toast.makeText(
                        requireContext(),
                        "Error en la navegacion",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        validateEmailFormat.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { eventoValidarEmail ->
                when (eventoValidarEmail) {
                    LoginViewModel.EmailFormat.IncorrectFormat -> {
                        binding.fragmentLoginEmail.helperText =
                            "El formato del email no es correcto" +
                                    " un ejemplo so tuviera una cuenta de google seria" +
                                    " example@gmail.com"
                        binding.fragmentLoginIniciarSesion.isEnabled = false
                    }
                    LoginViewModel.EmailFormat.CorrectFormat -> {
                        binding.fragmentLoginEmail.helperText = "Obligatorio*"
                        binding.fragmentLoginIniciarSesion.isEnabled = true
                    }
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

        progress.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { visible ->
                binding.circularProgressIndicator.isVisible = visible
            }
        }
    }

    companion object {
        private const val WEB_CLIENT_ID =
            "111012438267-2tlousk8k6u925h5cc6jhbsj8eq7fear.apps.googleusercontent.com"
    }
}
