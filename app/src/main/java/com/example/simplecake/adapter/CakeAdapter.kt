package com.example.simplecake.adapter

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CakeAdapter(var context: Context, var arrayCake:ArrayList<Cake1>):
    RecyclerView.Adapter<CakeAdapter.ViewHolder>() {
    val firebaseService = FirebaseService()
    lateinit var progressDialog: ProgressDialog
    var onItemClick : ((Cake1)-> Unit)? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeAdapter.ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item_cake,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        progressDialog = ProgressDialog(context)

        firebaseService.getOnlyImage(arrayCake[position].image!!) {

            Glide.with(context).load(it).into(holder.image)

        }
        holder.tvName.text = arrayCake[position].name
        holder.tvdesc.text = arrayCake[position].desc
        val dcf =DecimalFormat("#,###")
        val dcfs = DecimalFormatSymbols(Locale.getDefault())
        dcfs.groupingSeparator='.'
        dcf.decimalFormatSymbols=dcfs
        holder.tvprice.text =dcf.format(arrayCake[position].price) + " đ"

    }

    override fun getItemCount(): Int {
        return arrayCake.size
    }
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvName= view.findViewById<TextView>(R.id.tv_cake)
        val image = view.findViewById<ImageView>(R.id.img_cake)
        val tvprice = view.findViewById<TextView>(R.id.tv_price)
        val tvdesc = view.findViewById<TextView>(R.id.tv_desc)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(arrayCake[adapterPosition])

            }
        }
    }
}