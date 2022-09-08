package com.ayia.fxconverter.util

import android.content.Context
import java.util.*


class UserPreferencesRepository (context: Context){

    private val sharedPreferences =
        context.applicationContext.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
    /**
     * Get the appTheme. By default, theme is set to follow system.
     */
    val appTheme: Theme
        get() {
            val theme = sharedPreferences.getString(PREF_NAME_THEME_MODE, Theme.FOLLOW_SYSTEM.name)
            return Theme.valueOf(theme ?: Theme.FOLLOW_SYSTEM.name)
        }

    val currency : Currency
    get() {
        val code = sharedPreferences.getString(PREF_NAME_COUNTRY_CODE, Currency.getInstance(Locale.getDefault()).currencyCode)
        return Currency.getInstance(code)
    }



    fun updateTheme(theme: Theme) {
        sharedPreferences.edit()
            .putString(PREF_NAME_THEME_MODE, theme.name)
            .apply()

        ThemeChanger().invoke(theme)
    }


    fun updateIsFirstInstall(isFirst: Boolean){
        sharedPreferences.edit()
            .putBoolean(PREF_NAME_IS_FIRST_INSTALL, isFirst)
            .apply()
    }



    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val instance = UserPreferencesRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }

}