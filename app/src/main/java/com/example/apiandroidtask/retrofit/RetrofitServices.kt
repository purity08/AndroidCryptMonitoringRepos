package com.example.apiandroidtask.retrofit

import com.example.apiandroidtask.retrofit.Model.Cryptocurrencies
import com.example.apiandroidtask.retrofit.Model.Bitcoin
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitServices {
    @GET("assets")
    fun getAssets(): Call<Cryptocurrencies>

    @GET("assets/bitcoin")
    fun getBitcoinProperties(): Call<Bitcoin>

}