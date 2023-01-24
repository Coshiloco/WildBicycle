package com.exmaple.wildbicycle.ui.record

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exmaple.wildbicycle.BuildConfig
import com.exmaple.wildbicycle.R
import com.exmaple.wildbicycle.databinding.FragmentLoginBinding
import com.exmaple.wildbicycle.databinding.FragmentRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordFragment : Fragment() {

    private lateinit var viewModel: RecordViewModel
    private lateinit var binding: FragmentRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()
    }

    private fun setObservers() {
        TODO("Not yet implemented")
    }

    private fun setListeners() {

    }

}