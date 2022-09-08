package com.ayia.fxconverter.presentation.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ayia.fxconverter.R
import com.ayia.fxconverter.databinding.FragmentRateBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RateFragment : BottomSheetDialogFragment() {

    private lateinit var code: String
    private lateinit var baseCode: String

    @Inject
    internal lateinit var currencyAssistedFactory: RateViewModel.CurrencyAssistedFactory

    val vm : RateViewModel by viewModels {
        RateViewModel.provideFactory(currencyAssistedFactory, code, baseCode)
    }

    private var _binding : FragmentRateBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

        requireArguments().apply {
            code = getString("code", null)
            baseCode= getString("base_code", null)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = vm
        binding.lifecycleOwner = viewLifecycleOwner
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(code: String, base_code: String) =
            RateFragment().apply {
                arguments = Bundle().apply {
                    putString("code", code)
                    putString("base_code", base_code)
                }
            }
    }
}