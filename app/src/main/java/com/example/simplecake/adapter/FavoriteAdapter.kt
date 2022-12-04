package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.favorite
import com.example.simplecake.service.FirebaseService
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class FavoriteAdapter(var context: Context,var listFavorite:ArrayList<favorite>):RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var firebaseService=FirebaseService()
    var onItemClick : ((favorite)-> Unit)? =null
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvName= view.findViewById<TextView>(R.id.tv_cake)
        val image = view.findViewById<ImageView>(R.id.img_cake)
        val tvprice = view.findViewById<TextView>(R.id.tv_price)
        val tvdesc = view.findViewById<TextView>(R.id.tv_desc)
        init {
                            view.setOnClickListener {
                    onItemClick?.invoke(listFavorite[adapterPosition])

                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cake,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getCake(listFavorite[position].idCake){cake->
        firebaseService.getOnlyImage(cake.image!!) {

            Glide.with(context).load(it).into(holder.image)

        }
        holder.tvName.text = cake.name
        holder.tvdesc.text = cake.desc
        val dcf = DecimalFormat("#,###")
        val dcfs = DecimalFormatSymbols(Locale.getDefault())
        dcfs.groupingSeparator='.'
        dcf.decimalFormatSymbols=dcfs
        holder.tvprice.text =dcf.format(cake.price) + " đ"
    }
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }
}