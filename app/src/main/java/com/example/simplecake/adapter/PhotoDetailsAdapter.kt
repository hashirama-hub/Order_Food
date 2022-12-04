package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.PhotoCakeDetails1
import com.example.simplecake.service.FirebaseService

class PhotoDetailsAdapter(var context: Context, var listPhotoDetails:ArrayList<PhotoCakeDetails1>):RecyclerView.Adapter<PhotoDetailsAdapter.ViewHolder>() {
    val firebaseService = FirebaseService()
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val imgPhotoDetails = view.findViewById<ImageView>(R.id.img_cake_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_details,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getOnlyImage(listPhotoDetails[position].imgID!!) {
            Glide.with(context).load(it).into(holder.imgPhotoDetails)
        }
    }

    override fun getItemCount(): Int {
        return listPhotoDetails.size
    }
}