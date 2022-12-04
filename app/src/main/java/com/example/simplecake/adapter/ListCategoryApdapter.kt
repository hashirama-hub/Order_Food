package com.example.simplecake.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService
import de.hdodenhof.circleimageview.CircleImageView

class ListCategoryApdapter(var context: Context, var listCategory:ArrayList<category1>):
    RecyclerView.Adapter<ListCategoryApdapter.CategoryListViewHolder>() {
    var onItemClick : ((category1)-> Unit)? =null
    private val firebaseService = FirebaseService()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_listcategory,parent,false)
        return CategoryListViewHolder(view)
    }
    override fun getItemCount(): Int {
       return listCategory.size
    }


    override fun onBindViewHolder(holder: CategoryListViewHolder, position: Int) {
        firebaseService.getOnlyImage(listCategory[position].id!!) {
            Log.e("test",it.toString())
            Glide.with(context).load(it).into(holder.img_categories)
        }
        holder.tvNameCate.text = listCategory[position].nameCategory
         }



    inner class CategoryListViewHolder(view: View):RecyclerView.ViewHolder(view){
        val tvNameCate =  view.findViewById<TextView>(R.id.tv_list_category)

        val img_categories = view.findViewById<CircleImageView>(R.id.img_categories)
        init {
            view.setOnClickListener {
                onItemClick?.invoke(listCategory[adapterPosition])

            }
        }
//        fun bind(uri: Uri) {
//
//        }
    }
}