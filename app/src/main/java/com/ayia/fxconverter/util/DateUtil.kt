@file:JvmName("DateUtil")

package com.ayia.fxconverter.util

import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


fun toDateString(systemTime: Long): String {
    val sdf = SimpleDateFormat("E, dd LLL yyyy", Locale.getDefault())
    return sdf.format(systemTime)
}

fun formatDateToString(text: String): String {

    val inFormat = SimpleDateFormat("E, dd LLL yyyy hh:mm:ss z", Locale.getDefault())

    val date: Date = inFormat.parse(text)!!

    val outFormat = SimpleDateFormat("E, dd LLL yyyy", Locale.getDefault())

    Timber.tag(BASE_TAG).d("$outFormat.format(date) ")

    return outFormat.format(date)
}


fun formatDateToLong(text: String): Long {

    val inFormat = SimpleDateFormat("E, dd LLL yyyy hh:mm:ss z", Locale.getDefault())

    val date: Date = inFormat.parse(text)!!

    return date.time
}




