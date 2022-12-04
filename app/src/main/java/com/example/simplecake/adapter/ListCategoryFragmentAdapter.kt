package com.example.simplecake.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService

class ListCategoryFragmentAdapter(var context: Context,var listCategory:ArrayList<category1>)
    :RecyclerView.Adapter<ListCategoryFragmentAdapter.viewHolder>() {
    var onItemClick : ((category1)-> Unit)? =null
    private val firebaseService = FirebaseService()
    inner class viewHolder(view:View):RecyclerView.ViewHolder(view){
        val tvNameCate = view.findViewById<TextView>(R.id.tv_name_category)
        val img_avt_cate = view.findViewById<ImageView>(R.id.img_avatar_category)
        val tvDesc = view.findViewById<TextView>(R.id.tv_desc_categories)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(listCategory[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_categories_fragment,parent,false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {

        holder.tvNameCate.text = listCategory[position].nameCategory
        firebaseService.getOnlyImage(listCategory[position].id!!) {
            Log.e("test",it.toString())
            Glide.with(context).load(it).into(holder.img_avt_cate)
        }
        holder.tvDesc.text = listCategory[position].desc
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }
}