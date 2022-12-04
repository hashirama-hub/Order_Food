package com.example.simplecake.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.simplecake.*
import com.example.simplecake.adapter.CakeAdapter
import com.example.simplecake.adapter.CommentApdater
import com.example.simplecake.adapter.PhotoDetailsAdapter
import com.example.simplecake.adapter.PhotoListDetailAdapter
import com.example.simplecake.model.*
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.round

class Cake_Details_Activity : AppCompatActivity() {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var mViewPager2: ViewPager2

    lateinit var tv_name_cake:TextView
    lateinit var tv_price_cake:TextView
    lateinit var tv_desc_cake:TextView
    lateinit var rcv_list_photo:RecyclerView
    lateinit var btn_plus:Button
    lateinit var btn_minus:Button
    lateinit var tv_price_qualities:Button
    lateinit var et_qualities:EditText
    lateinit var rcv_comment:RecyclerView
    lateinit var rcv_cake:RecyclerView
    lateinit var btn_add_to_cart:Button
    lateinit var btn_favourite:ImageView
    lateinit var first_star:ImageView
    lateinit var second_star:ImageView
    lateinit var third_star:ImageView
    lateinit var forth_star:ImageView
    lateinit var fifth_star:ImageView
    lateinit var tip_options:RadioGroup
    lateinit var one_star:RadioButton
    lateinit var two_star:RadioButton
    lateinit var three_star:RadioButton
    lateinit var four_star:RadioButton
    lateinit var five_star:RadioButton
    lateinit var btn_submit:Button
    lateinit var tv_viewall:RelativeLayout
    lateinit var et_comment:EditText
    lateinit var empty_notfication:TextView
    lateinit var tv_star:TextView
    lateinit var tv_quantity_comment:TextView
    var average_star:Float=0.0f
    var price_quality:Long = 0
     var quality:Int = 1
    var count_favorite:Int=0
    var cakeDetails = Cake1()
    var rating:Int = 0
    val firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cake_details)
        initUI()
        val gson = Gson()
        val jsonCake: String? = intent.getStringExtra("json_cake")
        val cake = gson.fromJson(jsonCake, Cake1::class.java)
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        firebaseService.getCake(cake.id){
            this.cakeDetails = it
            //        setting.viewpager2
            val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
            val iduser:String = user!!.uid
            firebaseService.getListFavorite(iduser){listFavorite->
                for(i in 0 until listFavorite.size){
                    if(listFavorite[i].idCake!!.equals(cakeDetails.id)){
                        count_favorite++
                    }

                }
                btn_favourite.setOnClickListener {
                    if(count_favorite==0){
                    firebaseService.getLastId("lastIdFavorite"){lastId->
                        val favorite= favorite("favorite${lastId}", cakeDetails.id,iduser)
                        firebaseService.insertFavorite(favorite){
                            if(it==true){
                                Toast.makeText(this,"Add ${cakeDetails.name} to your favorite list",Toast.LENGTH_SHORT).show()
                            }
                            else{
                                Toast.makeText(this,"Can't add ${cakeDetails.name} to your favorite list",Toast.LENGTH_SHORT).show()
                            }
                        }
                        firebaseService.updateLastId("lastIdFavorite",lastId){

                        }
                    }
                    }else{
                        Toast.makeText(this,"${cakeDetails.name} was in your favorite list",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            mViewPager2.offscreenPageLimit=3
            mViewPager2.clipToPadding=false
            mViewPager2.clipChildren=false

            val compositePageTransformer:CompositePageTransformer= CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(40))
            compositePageTransformer.addTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
                    var r:Float = 1- abs(position)
                    page.scaleY = 0.85f+r*0.15f
                }

            })
            mViewPager2.setPageTransformer(compositePageTransformer)
            toolbar.title = cake.name
            tv_name_cake.text=cakeDetails.name
            tv_desc_cake.text=cakeDetails.desc
            price_quality = cakeDetails.price!!
            price_quality *= quality
            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            tv_price_cake.text =dcf.format(cakeDetails.price) + " đ"
            val photoDetailsAdapter= cakeDetails.listPhoto?.let { PhotoDetailsAdapter(this, it) }
            mViewPager2.adapter = photoDetailsAdapter
            val linearLayoutManager2: LinearLayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
            rcv_list_photo.layoutManager=linearLayoutManager2
            val photoListDetailAdapter =
                cakeDetails.listPhoto?.let { it1 -> PhotoListDetailAdapter(this, it1) }
            rcv_list_photo.adapter = photoListDetailAdapter
            tv_price_qualities.text=dcf.format(price_quality) + " đ"
//            this.et_qualities.setText(quality)
            et_qualities.setText(quality.toString(), TextView.BufferType.EDITABLE)
            btn_plus.setOnClickListener {
                quality++
                et_qualities.setText(quality.toString(), TextView.BufferType.EDITABLE)
                price_quality = cakeDetails.price!!
                price_quality *= quality
                tv_price_qualities.text=dcf.format(price_quality) + " đ"
            }
            btn_minus.setOnClickListener {
                if(quality>1) {
                    quality--
                    et_qualities.setText(quality.toString(), TextView.BufferType.EDITABLE)
                    price_quality = cakeDetails.price!!
                    price_quality *= quality
                    tv_price_qualities.text = dcf.format(price_quality) + " đ"
                }
                else{
                    Toast.makeText(this,"The qualities > 0",Toast.LENGTH_SHORT).show()
                }
            }


            et_qualities.doAfterTextChanged {
                if (it != null) {
                    if (it.length > 0) {
                        quality = it.toString().toInt()
                        price_quality = cakeDetails.price!!
                        price_quality *= quality
                        tv_price_qualities.text = dcf.format(price_quality) + " đ"
                    } else {
                        quality = 1
                        et_qualities.setText("1", TextView.BufferType.EDITABLE)
                        price_quality = cakeDetails.price!!
                        price_quality *= quality
                        tv_price_qualities.text = dcf.format(price_quality) + " đ"
                    }
                }else{
                    quality = 1
                    et_qualities.setText("1", TextView.BufferType.EDITABLE)
                    price_quality = cakeDetails.price!!
                    price_quality *= quality
                    tv_price_qualities.text = dcf.format(price_quality) + " đ"
                }
            }
            firebaseService.getListCake(cakeDetails.idCategory){ listCake->
                listCake.remove(cakeDetails)
                val cakeAdapter = CakeAdapter(this,listCake)
                rcv_cake.adapter =cakeAdapter
                val linearLayoutManager1:LinearLayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
                rcv_cake.layoutManager=linearLayoutManager1
                if (cakeAdapter!= null) {
                    cakeAdapter.onItemClick={
                        val intent = Intent(this, Cake_Details_Activity::class.java)
                        val gson = Gson()
                        val json = gson.toJson(it)
                        intent.putExtra("json_cake",json)
                        ContextCompat.startActivity(this, intent, null)
                    }
                }
            }
            //get lastID

                btn_add_to_cart.setOnClickListener {
                    firebaseService.getLastId("lastIdCart"){
                            lastId->
                        //add to cart

                        val cart = Cart("cart${lastId}",iduser,cakeDetails.id,quality,price_quality)
                    firebaseService.insertCart(cart){status->
                   if (status) {
                       Toast.makeText(this, "Added this product to Cart", Toast.LENGTH_SHORT).show()
                   } else {
                       Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                   }
                    }
                    firebaseService.updateLastId("lastIdCart",lastId){
                    }
                }

            }

        }
        //rcv_comment
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL,false)
        rcv_comment.layoutManager=linearLayoutManager
        firebaseService.getListComment(cake.id.toString()){firstComment->
            if(firstComment.size==0){
                empty_notfication.text="There aren't' any comments for ${cake.name}"
            }
        val commentApdater = CommentApdater(this, firstComment)
            rcv_comment.adapter = commentApdater
        }

        //end rcv_comment
        //rcv_cake_lq


        //end rcv_cake
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //submit comment
        btn_submit.setOnClickListener {
            sumitComment(iduser,cake.id)
        }
        //list comment

        firebaseService.getListComment(cake.id.toString()) { listComment ->
            if (listComment.size > 0) {
                for (i in 0 until listComment.size) {
                    average_star += listComment[i].star.toFloat()
                }
                average_star = average_star / listComment.size

            }
            else{
                average_star=0.0f
            }
            tv_star.text="${String.format("%.1f",average_star)}/5"
            tv_quantity_comment.text="${listComment.size} Reviews"
            displayStar(average_star)

                tv_viewall.setOnClickListener {
                    if(listComment.size>0) {
                    intent = Intent(this, CommentActivity::class.java)
                    intent.putExtra("id_cake", cake.id)
                    startActivity(intent)
                }
                    else{
                        Toast.makeText(this,"There aren't' any comments for ${cake.name}",Toast.LENGTH_SHORT).show()
                    }
            }

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

    private fun sumitComment(iduser: String, id: String?) {
        val selecteId=tip_options.checkedRadioButtonId
        val stars = when (selecteId){
            R.id.one_star->1
            R.id.two_star->2
            R.id.three_star->3
            R.id.four_star->4
            R.id.five_star->5
            else -> 5
        }
        val sdf = SimpleDateFormat("yyyy.MM.dd")
        val timestamp = Timestamp(System.currentTimeMillis())
        var today=sdf.format(timestamp)
        var sTrcomment:String =et_comment.text.toString()
        firebaseService.getLastId("lastIdComment"){lastId->
            var comment=Comment(
                "comment${lastId}"
            ,iduser
            ,id
            ,stars
            ,sTrcomment
            ,today
            )
            firebaseService.insertComment(comment){
                if(it==true){
                    Toast.makeText(this,"Comment Successfully",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Comment Failed",Toast.LENGTH_SHORT).show()
                }
            }
            firebaseService.updateLastId("lastIdComment",lastId){

            }
        }
    }

    private fun getListPhotoDetails(idImg1:Int): ArrayList<PhotoCakeDetails> {
        val list:ArrayList<PhotoCakeDetails> = arrayListOf(
            PhotoCakeDetails(idImg1),
            PhotoCakeDetails(R.drawable.classic_signature_1),
            PhotoCakeDetails(R.drawable.classic_signature_2),
        )
        return list;
    }

    private fun initUI(){
        toolbar=findViewById(R.id.tool_bar_details)
        mViewPager2 = findViewById(R.id.viewpager2)

        tv_name_cake = findViewById(R.id.tv_name_cake_details)
        tv_price_cake = findViewById(R.id.tv_price_item_details)
        tv_desc_cake = findViewById(R.id.tv_desc_details)
        rcv_list_photo=findViewById(R.id.rcv_photo)
        btn_plus=findViewById(R.id.btn_plus)
        btn_minus=findViewById(R.id.btn_minus)
        tv_price_qualities=findViewById(R.id.tv_price_qualities)
        et_qualities=findViewById(R.id.et_qualities)
        rcv_comment=findViewById(R.id.rcv_comment)
        rcv_cake = findViewById(R.id.rcv_cakes)
        btn_add_to_cart=findViewById(R.id.add_to_cart)
        btn_favourite=findViewById(R.id.btn_favourite)
        tip_options=findViewById(R.id.tip_options)
        one_star=findViewById(R.id.one_star)
        two_star=findViewById(R.id.two_star)
        three_star=findViewById(R.id.three_star)
        four_star=findViewById(R.id.four_star)
        five_star=findViewById(R.id.five_star)
        btn_submit=findViewById(R.id.btn_submit)
        et_comment=findViewById(R.id.et_comment)
        empty_notfication=findViewById(R.id.empty_notfication)
        first_star=findViewById(R.id.first_start)
        second_star=findViewById(R.id.second_start)
        third_star=findViewById(R.id.third_start)
        forth_star=findViewById(R.id.fourth_start)
        fifth_star=findViewById(R.id.fifth_start)
        tv_star=findViewById(R.id.tv_star)
        tv_quantity_comment=findViewById(R.id.tv_quantity_comment)
        tv_viewall=findViewById(R.id.ln_viewall)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu= menuInflater.inflate(R.menu.menu_toolbar,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        else if(item.itemId== R.id.cart){
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}