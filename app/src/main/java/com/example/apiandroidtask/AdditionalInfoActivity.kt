package com.example.apiandroidtask


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.apiandroidtask.retrofit.Model.CryptData
import com.example.apiandroidtask.retrofit.Model.IntervalData
import com.example.apiandroidtask.retrofit.RetrofitServices
import com.example.apiandroidtask.singleton.Singleton
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

const val ONE_DAY_IN_MILS = 86400000L

class AdditionalInfoActivity : AppCompatActivity() {
    private lateinit var crypt: Call<CryptData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitServices::class.java)

        val end = System.currentTimeMillis()
        val start = end - ONE_DAY_IN_MILS

        crypt = retrofitService.get24hCryptInfo(Singleton.id, start, end)
        crypt.enqueue(object : Callback<CryptData> {

            override fun onResponse(call: Call<CryptData>, response: Response<CryptData>) {
                if (!response.isSuccessful) {
                    return
                }
                val priceList: List<IntervalData>? = response.body()?.data
                if (priceList != null) {
                    val graph = findViewById<GraphView>(R.id.graph)
//                    val stf = StaticLabelsFormatter(graph)

                    val a: Array<DataPoint?> = arrayOfNulls(23)
                    for ((index, crypt) in priceList.withIndex()) {
                        a[index] = DataPoint(index.toDouble(), crypt.priceUsd!!.toDouble())
                    }
                    val series = LineGraphSeries(a)
                    graph.addSeries(series)
                    graph.gridLabelRenderer.numHorizontalLabels = 13
                    graph.visibility = View.VISIBLE

                    val highPrice = series.highestValueY
                    val lowPrice = series.lowestValueY
                    val avgPrice = (highPrice + lowPrice) / 2

                    val highPriceView = findViewById<TextView>(R.id.highPriceView)
                    highPriceView.text = "$${String.format("%.2f", highPrice)}"

                    val lowPriceView = findViewById<TextView>(R.id.lowPriceView)
                    lowPriceView.text = "$${String.format("%.2f", lowPrice)}"

                    val avgPriceView = findViewById<TextView>(R.id.avgPriceView)
                    avgPriceView.text = "$${String.format("%.2f", avgPrice)}"
//                    stf.setHorizontalLabels(arrayOf("h1", "h2", "h3"))
//                    stf.setVerticalLabels(arrayOf("v1", "v2", "v3"))
//                    graph.gridLabelRenderer.labelFormatter = stf
                }
            }

            override fun onFailure(call: Call<CryptData>, t: Throwable) {
                Log.d("______FAIL_______", t.message.toString())
            }
        })

        val dateView = findViewById<TextView>(R.id.dateView)
        val nameView = findViewById<TextView>(R.id.cryptNameView)
        val symbolView = findViewById<ImageView>(R.id.cryptImageView)

        nameView.text = Singleton.name

        val formatter = SimpleDateFormat("dd-MMMM-YYYY")
        dateView.text = formatter.format(Date(System.currentTimeMillis())).replace("-", " ")

        Picasso.get()
            .load("https://static.coincap.io/assets/icons/${Singleton.symbol?.toLowerCase()}@2x.png")
            .placeholder(R.color.white)
            .error(R.drawable.ic_launcher_foreground)
            .fit()
            .into(symbolView)
    }
}