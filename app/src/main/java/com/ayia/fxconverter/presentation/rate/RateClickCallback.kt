package com.ayia.fxconverter.presentation.rate

import com.ayia.fxconverter.domain.models.Rate

interface RateClickCallback {

    fun onClick(rate : Rate, position: Int)

    fun onLongClick(rate: Rate, position: Int): Boolean{
        return false
    }

}