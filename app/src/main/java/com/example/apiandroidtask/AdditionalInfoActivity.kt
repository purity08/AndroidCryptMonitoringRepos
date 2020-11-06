package com.example.apiandroidtask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.apiandroidtask.retrofit.Data
import com.example.apiandroidtask.retrofit.Model.Bitcoin
import com.example.apiandroidtask.retrofit.Model.Cryptocurrencies
import com.example.apiandroidtask.retrofit.RetrofitServices
import com.example.apiandroidtask.singleton.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class AdditionalInfoActivity : AppCompatActivity() {
    private lateinit var crypt: Call<Bitcoin>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitServices::class.java)
        crypt = retrofitService.getBitcoinInfo(Singleton.id)
        crypt.enqueue(object : Callback<Bitcoin> {

            override fun onResponse(call: Call<Bitcoin>, response: Response<Bitcoin>) {
                if (!response.isSuccessful) {
                    return
                }
                val posts: String? = response.body()?.data?.id
                if (posts != null) {
                    Log.d("ID:____________", posts)
                }
            }

            override fun onFailure(call: Call<Bitcoin>, t: Throwable) {}
        })
        val textView = findViewById<TextView>(R.id.textView)
        val formatter = SimpleDateFormat("MM-dd")
        textView.text = formatter.format(Date(System.currentTimeMillis()))
    }
}