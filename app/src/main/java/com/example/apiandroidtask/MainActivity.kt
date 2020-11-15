package com.example.apiandroidtask

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.apiandroidtask.adapter.Adapter
import com.example.apiandroidtask.model.RecyclerData
import com.example.apiandroidtask.singleton.Singleton
import com.example.apiandroidtask.viewmodelview.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener {

    lateinit var recyclerViewAdapter: Adapter
    private var list = arrayListOf<RecyclerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        recyclerViewAdapter = Adapter(this, listOf(), this)
        recycler.adapter = recyclerViewAdapter
        initializeUi()
    }

    private fun initializeUi() {
        val viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
        viewModel.recyclerListData.observe(this, Observer<ArrayList<RecyclerData>> {
            if (it != null) {
                list = it
                recyclerViewAdapter.setDataList(list)
            }
        })
    }

    override fun onItemClick(position: Int, v: View?) {
        val clickedItem = list[position]
        Singleton.id = clickedItem.id.toString()
        Singleton.name = clickedItem.name.toString()
        Singleton.symbol = clickedItem.symbol.toString()
        if (v != null) {
            v.isClickable = false
        }
        startActivity(Intent(this, AdditionalInfoActivity::class.java))
    }


    override fun onResume() {
        recycler.adapter?.notifyDataSetChanged()
        super.onResume()
    }
}

