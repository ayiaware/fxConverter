package com.ayia.fxconverter.di

import android.content.Context
import com.ayia.fxconverter.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

@Module
@InstallIn(ActivityComponent::class)
object SettingsModule {

    @Provides
    @Named("themeArray")
    fun provideThemeArray(
        @ApplicationContext context: Context,
    ): Array<String> = context.resources.getStringArray(R.array.themes)


}