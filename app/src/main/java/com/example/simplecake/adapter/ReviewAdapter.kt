package com.example.simplecake.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.simplecake.R
import com.example.simplecake.model.Review

class ReviewAdapter(var context: Context,var listReview:ArrayList<Review>):PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view:View = LayoutInflater.from(container.context).inflate(R.layout.item_review,container,false)
        val imgAvatar:ImageView = view.findViewById(R.id.img_avatar_review)
        imgAvatar.setImageResource(R.drawable.default_user)
        view.findViewById<TextView>(R.id.tv_name_user_review).text = listReview[position].name_user
        view.findViewById<TextView>(R.id.tv_email).text = listReview[position].email
        view.findViewById<TextView>(R.id.tv_review).text = listReview[position].noidung
        //add view to viewgroup
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return listReview.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        // remove view
        container.removeView(`object` as View?)
    }
}