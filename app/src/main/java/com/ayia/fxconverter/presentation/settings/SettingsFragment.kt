package com.ayia.fxconverter.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels

import com.ayia.fxconverter.presentation.main.MainViewModel
import com.ayia.fxconverter.util.BASE_TAG
import com.ayia.fxconverter.R
import com.ayia.fxconverter.databinding.FragmentSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SettingsFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private val myTag: String = BASE_TAG + " " + SettingsFragment::class.java.simpleName

    private val vm: SettingsViewModel by viewModels()

    private val mainVm : MainViewModel by activityViewModels()

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listener = this

        binding.lifecycleOwner = viewLifecycleOwner

        binding.vm = vm
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(p0: View) {

        Timber.tag(myTag).d("Method onClick")


        when (p0.id) {

            R.id.layoutTheme -> {
                showThemeDialog()
            }
            R.id.layoutClearData ->{
                showClearDataDialog()
            }
            R.id.layoutAbout ->{
                showAboutFragment()
            }
            R.id.layoutRateApp ->{
                rateApp()
            }

        }

    }


    private fun showThemeDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.label_select_theme)
            .setSingleChoiceItems(R.array.themes, vm.themePos) { _, i ->

                vm.themePos = i

                Timber.tag(myTag).d("Theme Pos $i")

                vm.setTheme()

            }.show()
    }

    private fun showClearDataDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.label_clear_data)
            .setMessage(R.string.msg_clear_data)
            .setPositiveButton("Cancel") { _, _ ->
                dismiss()
            }
            .setNegativeButton("Clear"){_,_ ->
                vm.clearData()
                mainVm.refreshInfoAndRatesObs(true)
            }
            .show()

    }

    private fun showAboutFragment(){

        val tag: String = AboutFragment::class.java.simpleName
        val ft = requireActivity().supportFragmentManager.beginTransaction()
        val prev = requireActivity().supportFragmentManager.findFragmentByTag(tag)

        if (prev == null){

            val newFragment: BottomSheetDialogFragment = AboutFragment.newInstance()
            newFragment.show(ft, tag)

        }



    }

    private fun rateApp() {
        val url = "https://play.google.com/store/apps/details?id=com.ayia.fxconverter"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }


}