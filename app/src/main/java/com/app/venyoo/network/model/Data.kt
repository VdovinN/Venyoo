package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("data")
        var data: MutableList<Lead>? = null
)
