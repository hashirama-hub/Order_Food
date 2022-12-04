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
import com.example.simplecake.model.Cart
import com.example.simplecake.service.FirebaseService
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CheckoutAdapter(var context: Context,var listCart:ArrayList<Cart>):RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
        var firebaseService = FirebaseService()
        var quality:Int=0
    var onItemClick : ((Cart)-> Unit)? =null
inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img_cake_checkout = view.findViewById<ImageView>(R.id.img_cake_checkout)
        val name_cake_checkout=view.findViewById<TextView>(R.id.name_cake_checkout)
        val name_category_checkout=view.findViewById<TextView>(R.id.name_category_checkout)
        val price_cake_checkout = view.findViewById<TextView>(R.id.price_cake_checkout)
        val quality_checkout=view.findViewById<TextView>(R.id.qualities_checkout)
    init {
        view.setOnClickListener {
            onItemClick?.invoke(listCart[adapterPosition])

        }
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View= LayoutInflater.from(parent.context).inflate(R.layout.item_checkout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getCake(listCart[position].idCake) { cake ->
            firebaseService.getOnlyImage(cake.image!!) {
                Glide.with(context).load(it).into(holder.img_cake_checkout)
            }
            quality= listCart[position].qualities!!
            holder.quality_checkout.text="x${listCart[position].qualities}"
            holder.name_cake_checkout.text=cake.name
            firebaseService.getCate(cake.idCategory){
                holder.name_category_checkout.text=it.nameCategory
            }
            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            holder.price_cake_checkout.text =dcf.format(listCart[position].totalcost) + " đ"
        }
    }

    override fun getItemCount(): Int {
       return listCart.size
    }
}