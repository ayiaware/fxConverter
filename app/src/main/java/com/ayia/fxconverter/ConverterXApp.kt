package com.ayia.fxconverter

import android.app.Application
import com.ayia.fxconverter.util.UserPreferencesRepository
import com.ayia.fxconverter.util.ThemeChanger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class ConverterXApp : Application(){

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val theme = UserPreferencesRepository.getInstance(this).appTheme
        ThemeChanger().invoke(theme)

    }
}