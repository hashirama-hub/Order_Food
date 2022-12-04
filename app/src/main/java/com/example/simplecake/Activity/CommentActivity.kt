package com.example.simplecake.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.R
import com.example.simplecake.adapter.CommentApdater
import com.example.simplecake.service.FirebaseService

class CommentActivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var rcv_comment:RecyclerView
    lateinit var first_star: ImageView
    lateinit var second_star: ImageView
    lateinit var third_star: ImageView
    lateinit var forth_star: ImageView
    lateinit var fifth_star: ImageView
    lateinit var tv_star: TextView
    lateinit var tv_quantity_comment: TextView
    var average_star:Float=0.0f
    var firebaseService=FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        initUI()
        setSupportActionBar(toolbar)
        toolbar.title="Comments"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val idCake=intent.getStringExtra("id_cake")
        firebaseService.getListComment(idCake.toString()) { listComment ->
            if (listComment.size > 0) {
                for (i in 0 until listComment.size) {
                    average_star += listComment[i].star.toFloat()
                }
                average_star = average_star / listComment.size

            }
            else{
                average_star=0.0f
            }
            displayStar(average_star)
            tv_star.text="${String.format("%.1f",average_star)}/5"
            tv_quantity_comment.text="${listComment.size} Reviews"
            val commentApdater = CommentApdater(this, listComment)
            rcv_comment.adapter = commentApdater
            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            rcv_comment.layoutManager=linearLayoutManager
        }

    }
    private fun displayStar(averageStar: Float) {
        if(averageStar==0.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar>0 && averageStar<1){
            first_star.setImageResource(R.drawable.ic_baseline_star_half_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==1.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar>1 && averageStar<2){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_half_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==2.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar>2 && averageStar<3){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_half_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==3.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar>3 && averageStar<4){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_half_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar==4.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_unrate_24)
        }
        else if(averageStar>4 && averageStar<5){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_half_24)
        }
        else if(averageStar==5.0f){
            first_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            second_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            third_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            forth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
            fifth_star.setImageResource(R.drawable.ic_baseline_star_rate_24)
        }
    }
    fun initUI(){
        toolbar=findViewById(R.id.tool_bar_comment)
        rcv_comment=findViewById(R.id.rcv_comment)
        first_star=findViewById(R.id.first_start)
        second_star=findViewById(R.id.second_start)
        third_star=findViewById(R.id.third_start)
        forth_star=findViewById(R.id.fourth_start)
        fifth_star=findViewById(R.id.fifth_start)
        tv_star=findViewById(R.id.tv_star)
        tv_quantity_comment=findViewById(R.id.tv_quantity_comment)
    }
}