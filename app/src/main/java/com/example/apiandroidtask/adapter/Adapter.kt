package com.example.apiandroidtask.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiandroidtask.Model
import com.example.apiandroidtask.R
import com.example.apiandroidtask.retrofit.Data
import com.squareup.picasso.Picasso

class Adapter(
    private val context: Context,
    private val list: ArrayList<Model>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        val name: TextView = view.findViewById(R.id.nameView)
        val priceUsd: TextView = view.findViewById(R.id.priceView)
        val changePercent24Hr: TextView = view.findViewById(R.id.changeView)
        val symbol: ImageView = view.findViewById(R.id.imageView)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition,v)
        }
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int,v: View?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.crypt_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]

        val price: String = String.format("%.3f", data.priceUsd?.toFloat())
        val change: String = String.format("%.2f", data.changePercent24Hr?.toFloat())

        if (change.startsWith("-"))
            holder.changePercent24Hr.setTextColor(Color.RED)
        else
            holder.changePercent24Hr.setTextColor(Color.GREEN)

        holder.name.text = data.name
        holder.priceUsd.text = "$${price}"
        holder.changePercent24Hr.text = "${change}%"

        Picasso.get()
            .load("https://static.coincap.io/assets/icons/${data.symbol?.toLowerCase()}@2x.png")
            .placeholder(R.color.white)
            .error(R.drawable.ic_launcher_foreground)
            .fit()
            .into(holder.symbol)
    }

    override fun getItemCount(): Int {
        return list.count()
    }

}