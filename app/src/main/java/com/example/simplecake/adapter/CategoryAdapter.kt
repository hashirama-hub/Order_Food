package com.example.simplecake.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.Activity.Cake_Details_Activity
import com.example.simplecake.R
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService
import com.google.gson.Gson
import kotlin.collections.ArrayList

class CategoryAdapter(var context: Context, var arrayCategory:ArrayList<category1>) :RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){
    val firebaseService = FirebaseService()
    lateinit var progressDialog: ProgressDialog
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return CategoryViewHolder(view)
    }
    fun setDataCate(listCategory:ArrayList<category1>){
        this.arrayCategory = listCategory
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        progressDialog = ProgressDialog(context)
        val category: category1 = arrayCategory[position]
        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
        holder.rcv_categories.layoutManager = linearLayoutManager
        holder.tvNameCate.text = arrayCategory[position].nameCategory
        holder.tv_desc_category.text = arrayCategory[position].desc
        firebaseService.getListCake(category.id){ listCake->
            val cakeAdapter = CakeAdapter(context,listCake)
            holder.rcv_categories.adapter =cakeAdapter
            if (cakeAdapter!= null) {
                cakeAdapter.onItemClick={
                    val intent = Intent(context, Cake_Details_Activity::class.java)
                    val gson = Gson()
                    val json = gson.toJson(it)
                    intent.putExtra("json_cake",json)
                    startActivity(context,intent,null)
                }
            }
        }




    }

    override fun getItemCount(): Int {
        return arrayCategory.size
    }
    inner class CategoryViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvNameCate =  view.findViewById<TextView>(R.id.tv_category)
        val rcv_categories= view.findViewById<RecyclerView>(R.id.rcv_cakes)
        val tv_desc_category = view.findViewById<TextView>(R.id.desc_category)
    }
}