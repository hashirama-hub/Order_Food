package com.example.simplecake.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.CakeListApdater
import com.example.simplecake.R
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService
import com.google.gson.Gson

class ListCakeAtivity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var rcv_cakeList:RecyclerView
    val firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_cake_ativity)
        val gson = Gson()
        val jsonCate: String? = intent.getStringExtra("json_name_category")
        val cate = gson.fromJson(jsonCate, category1::class.java)
        initUI()
        val scrollView=findViewById<ScrollView>(R.id.scrollview_list_cake)
        scrollView.isSmoothScrollingEnabled = true
        toolbar.title = cate.nameCategory
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val layoutManager: LinearLayoutManager = LinearLayoutManager(this)
        rcv_cakeList.layoutManager = layoutManager

        firebaseService.getListCake(cate.id){ listCake ->
           listCake
            val cakeListApdater: CakeListApdater = CakeListApdater(this,listCake)

            if (cakeListApdater != null) {
                cakeListApdater.onItemClick={
                    val intent = Intent(this, Cake_Details_Activity::class.java)
                    val gson = Gson()
                    val json = gson.toJson(it)
                    intent.putExtra("json_cake",json)
                    startActivity(intent)
                }
            }
            rcv_cakeList.adapter=cakeListApdater
        }




    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initUI(){
        toolbar=findViewById(R.id.tool_bar)
        rcv_cakeList=findViewById(R.id.rcv_cakeList)
        rcv_cakeList.isNestedScrollingEnabled = false
    }
}