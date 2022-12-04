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
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class ProductAdminAdapter(var context: Context, var listProducts:ArrayList<Cake1>):RecyclerView.Adapter<ProductAdminAdapter.ViewHolder>() {
    var onItemClick : ((Cake1)-> Unit)? =null
    private val firebaseService = FirebaseService()
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img_cake_item = view.findViewById<ImageView>(R.id.img_cake_itemt)
        val tv_name_cake = view.findViewById<TextView>(R.id.tv_name_cake)
        val tv_price_cake = view.findViewById<TextView>(R.id.tv_price_item)
        val tv_desc_itemt = view.findViewById<TextView>(R.id.tv_desc_cake)
        var tv_category=view.findViewById<TextView>(R.id.tv_categories)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(listProducts[adapterPosition])

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_admin,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getOnlyImage(listProducts[position].id!!) {
            Glide.with(context).load(it).into(holder.img_cake_item)
        }
        firebaseService.getCate(listProducts[position].idCategory){
            holder.tv_category.text = it.nameCategory
        }
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        holder.tv_name_cake.text = listProducts[position].name
        val dcf = DecimalFormat("#,###")
        val dcfs = DecimalFormatSymbols(Locale.getDefault())
        dcfs.groupingSeparator='.'
        dcf.decimalFormatSymbols=dcfs
        holder.tv_price_cake.text =dcf.format(listProducts[position].price) + " đ"
        holder.tv_desc_itemt.text = listProducts[position].desc

    }

    override fun getItemCount(): Int {
        return listProducts.size
    }
}