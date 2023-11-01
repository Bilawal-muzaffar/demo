package com.jaco.data.service


import com.digiventure.ventnote.data.pojo.Currency
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthenticationService {

    @GET("latest")
    suspend fun getCurrencyList(@Query("access_key") key : String): Currency


}