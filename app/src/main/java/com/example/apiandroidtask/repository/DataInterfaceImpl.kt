package com.example.apiandroidtask.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.apiandroidtask.model.CryptData
import com.example.apiandroidtask.model.IntervalData
import com.example.apiandroidtask.model.RecyclerData
import com.example.apiandroidtask.model.RecyclerList
import com.example.apiandroidtask.network.RetroInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.apiandroidtask.singleton.Singleton


class DataInterfaceImpl : DataInterface {
    override fun getData(): LiveData<ArrayList<RecyclerData>> {
        val liveData = MutableLiveData<ArrayList<RecyclerData>>()

        Log.d("getData", RetroInstance.apiService.getCryptList().request().toString())
        RetroInstance
            .apiService
            .getCryptList()
            .enqueue(object : Callback<RecyclerList> {
                override fun onResponse(
                    call: Call<RecyclerList>,
                    response: Response<RecyclerList>
                ) {
                    Log.d("onResponse", response.message())
                    liveData.value = response.body()?.data
                }

                override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
                }
            })
        return liveData
    }

    override fun get24hData(): LiveData<ArrayList<IntervalData>> {

        val live24hData = MutableLiveData<ArrayList<IntervalData>>()
        RetroInstance
            .apiService
            .get24hCryptList(Singleton.id, Singleton.start, Singleton.end)
            .enqueue(object : Callback<CryptData> {
                override fun onResponse(call: Call<CryptData>, response: Response<CryptData>) {
                    Log.d("onResponse", response.message())
                    live24hData.value = response.body()?.data
                }

                override fun onFailure(call: Call<CryptData>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }
            })
        return live24hData
    }

}