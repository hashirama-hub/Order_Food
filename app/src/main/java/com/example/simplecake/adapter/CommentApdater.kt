package com.example.simplecake.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.model.Comment
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CommentApdater(var context: Context,var listComment:ArrayList<Comment>):RecyclerView.Adapter<CommentApdater.ViewHolder>() {
    var firebaseService=FirebaseService()
    val user1: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val img_avatar_cmt = view.findViewById<ImageView>(R.id.img_avatar_comment)
        val tv_name_user = view.findViewById<TextView>(R.id.tv_name_user)
        val tv_comment = view.findViewById<TextView>(R.id.tv_comment)
        val tv_day_comment=view.findViewById<TextView>(R.id.tv_day_comment)
        val first_star=view.findViewById<ImageView>(R.id.first_start)
        val second_star=view.findViewById<ImageView>(R.id.second_start)
        val third_star=view.findViewById<ImageView>(R.id.third_start)
        val forth_star=view.findViewById<ImageView>(R.id.fourth_start)
        val fifth_star=view.findViewById<ImageView>(R.id.fifth_start)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        firebaseService.getUser(listComment[position].idUser){ user->
            holder.tv_name_user.text = user.name
            val photoUrl=Uri.parse(user.avatar)
            Log.i("test", photoUrl.toString())
            Glide.with(context).load(photoUrl).error(R.drawable.default_user).into(holder.img_avatar_cmt)
        }
        holder.tv_comment.text = listComment[position].comment
        holder.tv_day_comment.text = "Posted on: "+listComment[position].dateComment
        val averageStar=listComment[position].star

        if(averageStar==0){
            holder.first_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.second_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }

        else if(averageStar==1){
            holder.first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.second_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==2){
            holder.first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }

        else if(averageStar==3) {
            holder.first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==4){
            holder.first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.forth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==5){
           holder.first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
           holder.second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
           holder.third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
           holder.forth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
           holder.fifth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
        }

    }

    override fun getItemCount(): Int {
        return listComment.size
    }

}