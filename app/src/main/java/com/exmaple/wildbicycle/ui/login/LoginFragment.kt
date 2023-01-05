package com.exmaple.wildbicycle.ui.login

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.exmaple.wildbicycle.BuildConfig
import com.exmaple.wildbicycle.R
import com.exmaple.wildbicycle.databinding.FragmentLoginBinding
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
            binding.fragmentLoginEmail.editText?.setText("a@a.com")
            binding.fragmentLoginPass.editText?.setText("abcde123")
        }

        setListeners()
        setObservers()
    }


    private fun setListeners() = with(binding) {
        fragmentLoginGoogleButton.setOnClickListener {
            viewModel.googleLogin(
                fragmentLoginEmail.editText?.text.toString(),
                fragmentLoginPass.editText?.text.toString(),
            )
        }
    }

    private fun setObservers() = with(viewModel) {
        navigation.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigate ->
                when (navigate) {
                    LoginViewModel.Navigate.HOME -> {
                        Toast.makeText(requireContext(), "Go To Home", Toast.LENGTH_SHORT).show()

//                    findNavController().navigate(R.id.homeFragment)
                        LoginFragmentDirections.actionNavLoginToHomeFragment().let { action ->
                            findNavController().navigate(action)
                        }
                    }

                    LoginViewModel.Navigate.BACK -> Toast.makeText(
                        requireContext(),
                        "Not implemented",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

        errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}