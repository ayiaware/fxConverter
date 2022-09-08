package com.ayia.fxconverter.presentation.options

import com.ayia.fxconverter.domain.models.Option

interface OptionClickCallback {

    fun onClick(option: Option)

}