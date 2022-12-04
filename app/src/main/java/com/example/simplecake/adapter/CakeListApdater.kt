package com.example.simplecake

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CakeListApdater(var context: Context, var listCake:ArrayList<Cake1>):RecyclerView.Adapter<CakeListApdater.ViewHolder>() {
    var onItemClick : ((Cake1)-> Unit)? =null
    private val firebaseService = FirebaseService()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_cake_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getOnlyImage(listCake[position].id!!) {
            Glide.with(context).load(it).into(holder.img_cake_item)
        }
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        holder.tv_name_cake.text = listCake[position].name
        val dcf = DecimalFormat("#,###")
        val dcfs = DecimalFormatSymbols(Locale.getDefault())
        dcfs.groupingSeparator='.'
        dcf.decimalFormatSymbols=dcfs
        holder.tv_price_cake.text =dcf.format(listCake[position].price) + " đ"
        holder.tv_desc_itemt.text = listCake[position].desc
        holder.ic_add_favorite.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return listCake.size
    }
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val img_cake_item = view.findViewById<ImageView>(R.id.img_cake_itemt)
        val tv_name_cake = view.findViewById<TextView>(R.id.tv_name_cake)
        val tv_price_cake = view.findViewById<TextView>(R.id.tv_price_item)
        val tv_desc_itemt = view.findViewById<TextView>(R.id.tv_desc_cake)
        val ic_add_favorite=view.findViewById<ImageView>(R.id.ic_add_favorite)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(listCake[adapterPosition])

            }
        }
    }
}