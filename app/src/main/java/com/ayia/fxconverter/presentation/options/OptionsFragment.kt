package com.ayia.fxconverter.presentation.options

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ayia.fxconverter.R
import com.ayia.fxconverter.databinding.FragmentOptionsBinding
import com.ayia.fxconverter.domain.models.Option
import com.ayia.fxconverter.presentation.main.MainViewModel
import com.ayia.fxconverter.util.DispatcherProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class OptionsFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentOptionsBinding? = null
    private val binding get() = _binding!!

    private var _adapter : OptionsAdapter? = null
    private val adapter get() = _adapter!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var optionType: String

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    companion object{
        @JvmStatic
        fun newInstance(optionType: String): OptionsFragment {
            val args = Bundle()
            args.putString("option_type", optionType)
            val fragment = OptionsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

        optionType = requireArguments().getString("option_type", null)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOptionsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = OptionsAdapter(clickCallback)

        binding.rvOptions.adapter = adapter

        val currencyCodes = resources.getStringArray(R.array.currency_codes)

        val options: MutableList<Option> = mutableListOf()

        for (c in currencyCodes) {
            options.add(Option(name = "${Currency.getInstance(c).displayName} ($c)", value = c))
        }
        options.distinctBy { it.value }.sortedBy { it.name }

        adapter.modifyList(options)

        binding.etSearch.requestFocus()

        var searchJob : Job? = null

        binding.etSearch.addTextChangedListener { text->
            searchJob?.cancel()

            searchJob = viewLifecycleOwner.lifecycleScope.launch(dispatcherProvider.io){

                withContext(dispatcherProvider.main){

                    delay(500L)

                    val query: Editable? = binding.etSearch.text
                    query?.let {
                        adapter.filter.filter(it)
                    }

                }
            }
        }

        binding.etSearch.setOnFocusChangeListener {_, hasFocus ->

            binding.tilSearch.isEndIconVisible = hasFocus

        }

    }


    private val clickCallback : OptionClickCallback = object : OptionClickCallback {
        override fun onClick(option: Option) {

            if (optionType == OptionType.FROM.name){
                mainViewModel.fromCurrencyOption.value = option
            }
            else if (optionType == OptionType.TO.name){
                mainViewModel.toCurrencyOption.value = option
            }

            dismiss()

        }
    }


    override fun onDestroyView() {
        _adapter = null
        _binding = null
        super.onDestroyView()
    }

}