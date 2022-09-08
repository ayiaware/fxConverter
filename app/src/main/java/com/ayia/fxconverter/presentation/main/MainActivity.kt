package com.ayia.fxconverter.presentation.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ayia.fxconverter.R
import com.ayia.fxconverter.databinding.ActivityMainBinding
import com.ayia.fxconverter.presentation.converter.ConverterFragment
import com.ayia.fxconverter.presentation.settings.SettingsFragment
import com.ayia.fxconverter.util.BASE_TAG
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var assistedFactory: MainViewModel.MainAssistedFactory

    private val viewModel: MainViewModel by viewModels{

        MainViewModelFactory(assistedFactory, this)

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->

            showConverterFragment()

        }



        binding.appBarMain.vm = viewModel

        binding.appBarMain.lifecycleOwner = this

        viewModel.shrinkFab.observe(this) { isShrink ->
            Timber.tag(BASE_TAG).d("isShrink $isShrink")

            if (isShrink)
                binding.appBarMain.fab.shrink()
            else
                binding.appBarMain.fab.extend()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val result = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.main_menu, menu)

        return result
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                showSettingsFragment()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSettingsFragment() {
        val tag: String = SettingsFragment::class.java.simpleName
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(tag)
        if (prev == null) {
            val newFragment: BottomSheetDialogFragment =
                SettingsFragment.newInstance()
            newFragment.show(ft, tag)
        }
    }


    private fun showConverterFragment() {
        val tag: String = ConverterFragment::class.java.simpleName
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(tag)
        if (prev == null) {
            val newFragment: BottomSheetDialogFragment =
                ConverterFragment.newInstance()
            newFragment.show(ft, tag)
        }
    }



}