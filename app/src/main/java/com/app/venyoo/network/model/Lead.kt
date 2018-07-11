package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Lead(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("is_test")
        var isTest: Int? = null,
        @SerializedName("owner_user_id")
        var ownerUserId: Int? = null,
        @SerializedName("host")
        var host: String? = null,
        @SerializedName("url")
        var url: String? = null,
        @SerializedName("first_last_name")
        var firstLastName: String? = null,
        @SerializedName("phone")
        var phone: String? = null,
        @SerializedName("email")
        var email: String? = null,
        @SerializedName("region")
        var region: String? = null,
        @SerializedName("question")
        var question: String? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("sex")
        var sex: String? = null,
        @SerializedName("sms")
        var sms: Int? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("geotag")
        var geotag: Geotag? = null,
        @SerializedName("is_mobile")
        var isMobile: Int? = null,
        @SerializedName("callback")
        var callback: Int? = null,
        @SerializedName("callData")
        var callData: CallData? = null,
        @SerializedName("coldpopup_trigger")
        var coldpopupTrigger: Int? = null,
        @SerializedName("dynamic_status")
        var dynamicStatus: Int? = null,
        @SerializedName("show")
        var show: String? = null,
        @SerializedName("show_ts")
        var showTs: Long? = null,
        @SerializedName("hasTransactions")
        var hasTransactions: Boolean? = null,
        @SerializedName("social")
        var social: String? = null,
        @SerializedName("socialData")
        var socialData: Boolean? = null,
        @SerializedName("room_id")
        var roomId: String? = null
) : Serializable
