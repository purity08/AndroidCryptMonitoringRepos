package com.example.apiandroidtask

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiandroidtask.adapter.Adapter
import com.example.apiandroidtask.retrofit.Data
import com.example.apiandroidtask.retrofit.Model.Cryptocurrencies
import com.example.apiandroidtask.retrofit.RetrofitServices
import com.example.apiandroidtask.singleton.Singleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


const val BASE_URL = "https://api.coincap.io/v2/"

class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener {

    private lateinit var crypt: Call<Cryptocurrencies>
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView
    private val namesList = arrayListOf<Model>()
    lateinit var globalTimer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler)
        textView = findViewById(R.id.staticNameView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitServices::class.java)

        crypt = retrofitService.getAssets()
        crypt.enqueue(object : Callback<Cryptocurrencies> {
            override fun onResponse(
                call: Call<Cryptocurrencies>,
                response: Response<Cryptocurrencies>
            ) {
                if (!response.isSuccessful) {
                    return
                }
                val posts: List<Data>? = response.body()?.data
                if (posts != null) {
                    for (crypt in posts) {
                        namesList.add(
                            Model(
                                crypt.id,
                                crypt.name,
                                crypt.symbol,
                                crypt.priceUsd,
                                crypt.changePercent24Hr
                            )
                        )
                    }
                }
                recyclerView.adapter = Adapter(baseContext, namesList, this@MainActivity)
            }

            override fun onFailure(call: Call<Cryptocurrencies>, t: Throwable) {}
        })
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

    }

    override fun onItemClick(position: Int, v: View?) {
        globalTimer.cancel()
        val clickedItem = namesList[position]
        Singleton.id = clickedItem.id.toString()
        Singleton.name = clickedItem.name.toString()
        Singleton.symbol = clickedItem.symbol.toString()

        startActivity(Intent(this, AdditionalInfoActivity::class.java))
    }

    private fun setValues(timer: Timer): Timer {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread { Log.d("QWEQWR", "1") }
            }
        }, 0, 3500)
        return timer
    }

    override fun onResume() {
        globalTimer = setValues(Timer())
        super.onResume()
    }
}

