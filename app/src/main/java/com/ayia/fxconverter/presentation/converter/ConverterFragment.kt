package com.ayia.fxconverter.presentation.converter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ayia.fxconverter.R
import com.ayia.fxconverter.data.Resource
import com.ayia.fxconverter.databinding.FragmentConverterBinding
import com.ayia.fxconverter.presentation.main.MainViewModel
import com.ayia.fxconverter.presentation.options.OptionType
import com.ayia.fxconverter.presentation.options.OptionsFragment
import com.ayia.fxconverter.util.BASE_TAG
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ConverterFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentConverterBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private val vm : ConverterViewModel by viewModels()

    private val TAG = BASE_TAG + " " + ConverterFragment::class.simpleName

    private lateinit var codes: Array<String>

    //https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda

    companion object {
        @JvmStatic
        fun newInstance() = ConverterFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConverterBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm

        binding.lifecycleOwner = viewLifecycleOwner

         codes = resources.getStringArray(R.array.currency_codes)

        binding.tilFromCurrency.setEndIconOnClickListener {
            showOptionDialog(OptionType.FROM)
        }
        binding.etFromCurrency.setOnClickListener {
            showOptionDialog(OptionType.FROM)
        }
        binding.etFromCurrency.setOnFocusChangeListener{_, isFocused ->
            if (isFocused)
                showOptionDialog(OptionType.FROM)
        }

        binding.tilToCurrency.setEndIconOnClickListener {
            showOptionDialog(OptionType.TO)
        }
        binding.etToCurrency.setOnClickListener {
            showOptionDialog(OptionType.TO)
        }
        binding.etToCurrency.setOnFocusChangeListener{_, isFocused ->
            if (isFocused)showOptionDialog(OptionType.TO)
        }



        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                Timber.tag(TAG).d("lifecycleScope.launchWhenStarted")
//
//            }
            vm.conversion.collect { event ->

                Timber.tag(TAG).d("viewModel.conversion.collect")

                binding.layoutResult.visibility = View.VISIBLE

                when (event) {
                    is Resource.Success -> {
                        binding.tvResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSecondary))
                        vm.resetButton()
                        Timber.tag(TAG).d("success")
                        delay(500L)
                        binding.progressBar.visibility = View.GONE
                        binding.tvResult.text = event.data
                        binding.tvResult.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        Timber.tag(TAG).d("error")
                        binding.tvResult.setTextColor(Color.RED)
                        delay(500L)
                        binding.btnConvert.isEnabled = true
                        binding.progressBar.visibility = View.GONE
                        binding.tvResult.text = getString(event.txtId)
                        binding.tvResult.visibility = View.VISIBLE
                    }
                    is Resource.Loading -> {
                        Timber.tag(TAG).d("loading")
                        binding.tvResult.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.progressBar.visibility = View.GONE
                        Timber.tag(TAG).d("empty")
                    }
                }

            }

        }


        mainViewModel.fromCurrencyOption.observe(viewLifecycleOwner) {
            vm.fromCurrencyOption.value = it
        }

        mainViewModel.toCurrencyOption.observe(viewLifecycleOwner) {
            vm.toCurrencyOption.value = it
        }


    }

    private fun showOptionDialog(optionType: OptionType) {

        val tag = OptionsFragment::class.java.simpleName
        val ft = requireActivity().supportFragmentManager.beginTransaction()
        val prev = requireActivity().supportFragmentManager.findFragmentByTag(tag)
        if (prev == null) {
            val newFragment: BottomSheetDialogFragment =
                OptionsFragment.newInstance(optionType.name)
            newFragment.show(ft, tag)
        }

    }


    override fun onDestroyView() {
        _binding = null
        mainViewModel.resetOptions()
        super.onDestroyView()
    }



}