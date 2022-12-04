package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.Photo

class PhotoAdapter(var context: Context, var photoList:ArrayList<Photo>): PagerAdapter() {
    override fun getCount(): Int {
        return photoList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view:View = LayoutInflater.from(container.context).inflate(R.layout.item_slider,container,false)
        val imgPhoto:ImageView = view.findViewById(R.id.img_photo)
        val photo: Photo = photoList[position]

            Glide.with(context).load(photo.imageID).into(imgPhoto)

        container.addView(view)
        view.findViewById<TextView>(R.id.name_slide).text = photo.name_slide
        view.findViewById<TextView>(R.id.desc_slider).text = photo.desc_slide

        //add view to viewgroup
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // remove view
        container.removeView(`object` as View?)
    }

}