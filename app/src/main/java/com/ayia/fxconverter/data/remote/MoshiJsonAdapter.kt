package com.ayia.fxconverter.data.remote


import com.ayia.fxconverter.data.remote.dto.InfoAndRatesResponse
import com.ayia.fxconverter.domain.models.Rate

import com.squareup.moshi.*
import timber.log.Timber


    class MoshiJsonAdapter : JsonAdapter<InfoAndRatesResponse>() {

    @FromJson
    override fun fromJson(reader: JsonReader): InfoAndRatesResponse {

        reader.beginObject()

        var result = ""
        var timeLastUpdateUnix = 0
        var timeLastUpdateUtc = ""
        var timeNextUpdateUnix = 0
        var timeNextUpdateUtc = ""
        var baseCode = ""
        val rates = mutableListOf<Rate>()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "result" -> result = reader.nextString()
                "time_last_update_unix" -> timeLastUpdateUnix = reader.nextInt()
                "time_last_update_utc" -> timeLastUpdateUtc = reader.nextString()
                "time_next_update_unix" -> timeNextUpdateUnix = reader.nextInt()
                "time_next_update_utc" -> timeNextUpdateUtc = reader.nextString()
                "base_code" -> baseCode = reader.nextString()
                "conversion_rates" -> {

                    reader.beginObject()

                    while (reader.peek() != JsonReader.Token.END_OBJECT) {

                        val code = reader.nextName()

                        val amount = when (code) {
                            baseCode -> reader.nextInt().toDouble()
                            else -> reader.nextDouble()
                        }

                        val infoBaseCode = baseCode

                        Timber.tag("MyTag")
                            .d(" code $code amount $amount infoBaseCode $infoBaseCode")

                        rates.add(
                            Rate(
                                code = code,
                                amount = amount,
                                infoBaseCode = infoBaseCode,
                            )
                        )
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return InfoAndRatesResponse(
            result = result,
            timeLastUpdateUnix = timeLastUpdateUnix,
            timeLastUpdateUtc = timeLastUpdateUtc,
            timeNextUpdateUnix = timeNextUpdateUnix,
            timeNextUpdateUtc = timeNextUpdateUtc,
            baseCode = baseCode,
            rates = rates
        )
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: InfoAndRatesResponse?) {
        throw UnsupportedOperationException()
    }
}