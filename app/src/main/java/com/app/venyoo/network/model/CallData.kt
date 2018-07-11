package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CallData(
        @SerializedName("call_status")
        var callStatus: Boolean? = null,
        @SerializedName("duration")
        var duration: Int? = null,
        @SerializedName("call1")
        var call1: Int? = null,
        @SerializedName("call2")
        var call2: Int? = null,
        @SerializedName("record_url")
        var recordUrl: Boolean? = null,
        @SerializedName("waitingForStats")
        var waitingForStats: Boolean? = null,
        @SerializedName("oneWayCall")
        var oneWayCall: Boolean? = null
): Serializable
