package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Status(
        @SerializedName("id")
        var id: Long,
        @SerializedName("name")
        var name: String,
        @SerializedName("color")
        var color: String,
        @SerializedName("order")
        var order: Int
): Serializable