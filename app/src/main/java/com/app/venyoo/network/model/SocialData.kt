package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SocialData(
        @SerializedName("first_name")
        var firstName: String? = null,
        @SerializedName("last_name")
        var lastName: String? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("photo")
        var photo: String? = null,
        @SerializedName("photo_big")
        var photoBig: String? = null,
        @SerializedName("city")
        var city: String? = null,
        @SerializedName("profile")
        var profile: String? = null,
        @SerializedName("network")
        var network: String? = null
) : Serializable