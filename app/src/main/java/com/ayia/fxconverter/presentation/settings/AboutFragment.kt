package com.ayia.fxconverter.presentation.settings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.content.Intent
import android.net.Uri
import com.ayia.fxconverter.R
import com.ayia.fxconverter.databinding.FragmentAboutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AboutFragment : BottomSheetDialogFragment(), View.OnClickListener {

    private var _binding: FragmentAboutBinding? = null

    private val binding get() = _binding!!

    companion object{
        @JvmStatic
        fun newInstance() = AboutFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.listener = this
    }


    override fun onClick(p0: View) {

        when (p0.id) {


            R.id.ivSocial1 -> {
                composeEmail()
            }

            R.id.ivSocial2 ->{
                openWebPage(getString(R.string.ayia_instagram))
            }

            R.id.ivSocial3 ->{
                openWebPage(getString(R.string.ayia_twitter))
            }
            R.id.ivSocial4 ->{
                openWebPage(getString(R.string.ayia_github))
            }



        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.ayia_email))
//        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }


    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}