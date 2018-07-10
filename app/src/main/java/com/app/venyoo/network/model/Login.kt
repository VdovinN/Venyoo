package com.app.venyoo.network.model

import com.google.gson.annotations.SerializedName

data class Login(@SerializedName("token") val token: String)