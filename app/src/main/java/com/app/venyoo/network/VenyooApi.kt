package com.app.venyoo.network

import com.app.venyoo.network.model.Data
import com.app.venyoo.network.model.Login
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface VenyooApi {

    @POST("/rest/auth")
    fun login(@Query("email") email: String, @Query("password") password: String): Observable<Response<Login>>

    @POST("/rest/leads")
    fun getLeads(@Query("token") token: String): Observable<Data>

}