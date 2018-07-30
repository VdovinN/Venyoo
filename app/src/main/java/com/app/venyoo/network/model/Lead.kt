package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Lead(
        @SerializedName("id")
        val id: Int,
        @SerializedName("is_test")
        val isTest: Int,
        @SerializedName("owner_user_id")
        val ownerUserId: Int,
        @SerializedName("host")
        val host: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("first_last_name")
        val firstLastName: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("region")
        val region: String,
        @SerializedName("question")
        val question: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("sex")
        val sex: String,
        @SerializedName("sms")
        val sms: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("geotag")
        var geotag: Geotag? = null,
        @SerializedName("is_mobile")
        val isMobile: Int,
        @SerializedName("callback")
        val callback: Int,
        @SerializedName("callData")
        var callData: CallData? = null,
        @SerializedName("coldpopup_trigger")
        val coldpopupTrigger: Int,
        @SerializedName("dynamic_status")
        val dynamicStatus: Int,
        @SerializedName("show")
        val show: String,
        @SerializedName("show_ts")
        val showTs: Long,
        @SerializedName("hasTransactions")
        val hasTransactions: Boolean,
        @SerializedName("social")
        val social: String,
        @SerializedName("socialData")
        var socialData: SocialData? = null,
        @SerializedName("room_id")
        val roomId: String
) : Serializable
