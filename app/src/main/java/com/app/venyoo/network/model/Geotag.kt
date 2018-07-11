package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Geotag(
        @SerializedName("geotag")
        var geotag: String? = null,
        @SerializedName("city")
        var city: String? = null,
        @SerializedName("city_code")
        var cityCode: String? = null,
        @SerializedName("country")
        var country: String? = null,
        @SerializedName("country_code")
        var countryCode: String? = null,
        @SerializedName("region")
        var region: String? = null,
        @SerializedName("region_code")
        var regionCode: String? = null,
        @SerializedName("status")
        var status: String? = null
) : Serializable
