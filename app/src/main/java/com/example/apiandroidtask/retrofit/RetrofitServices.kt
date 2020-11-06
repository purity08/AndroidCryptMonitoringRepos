package com.example.apiandroidtask.retrofit

import com.example.apiandroidtask.retrofit.Model.Bitcoin
import com.example.apiandroidtask.retrofit.Model.Cryptocurrencies
import com.example.apiandroidtask.singleton.Singleton
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitServices {
    @GET("assets")
    fun getAssets(): Call<Cryptocurrencies>

    @GET("assets/{id}")
    fun getBitcoinInfo(@Path("id")s: String):Call<Bitcoin>
}