package com.example.apiandroidtask.retrofit

import com.example.apiandroidtask.retrofit.Model.CryptData
import com.example.apiandroidtask.retrofit.Model.Cryptocurrencies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServices {
    @GET("assets")
    fun getAssets(): Call<Cryptocurrencies>

    @GET("assets/{id}/history?interval=h1")
    fun getCryptInfo(@Path("id") s: String): Call<CryptData>

    @GET("assets/{id}/history?interval=m1")
    fun get24hCryptInfo(
        @Path("id") id: String,
        @Query("start") start: Long,
        @Query("end") end: Long
    ): Call<CryptData>

}