package com.example.simplecake.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.adapter.CartAdapter
import com.example.simplecake.ItemTouchHelperListener
import com.example.simplecake.R
import com.example.simplecake.RecyclerViewItemTouchHelper
import com.example.simplecake.model.Cart
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : AppCompatActivity(), ItemTouchHelperListener {
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var rcv_cart:RecyclerView
    lateinit var tv_total_price:TextView
    lateinit var btn_checkout:Button
    lateinit var arrayCart:ArrayList<Cart>
    lateinit var cartAdapter: CartAdapter
    var firebaseService=FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initUI()
        setSupportActionBar(toolbar)
        toolbar.title="Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        rcv_cart.isNestedScrollingEnabled=false
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        firebaseService.getCartList(iduser){
            arrayCart = it
            cartAdapter= CartAdapter(this,it)
            rcv_cart.adapter=cartAdapter
//            val itemDecoration:RecyclerView.ItemDecoration=DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
//            rcv_cart.addItemDecoration(itemDecoration)
//
            val simpleCallback:ItemTouchHelper.SimpleCallback= RecyclerViewItemTouchHelper(0,ItemTouchHelper.LEFT,this)
            ItemTouchHelper(simpleCallback).attachToRecyclerView(rcv_cart)

            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
            rcv_cart.layoutManager = linearLayoutManager
            cartAdapter.OnChange = {
                if(it==true){
                    calTotalPrice(iduser)
                }
            }
            btn_checkout.setOnClickListener {
                firebaseService.getCartList(iduser) {result->
                    arrayCart=result
                    if (arrayCart.size>0) {
                        val intent = Intent(this, CheckoutActivity::class.java)
                        val gson = Gson()
                        val json = gson.toJson(arrayCart)
                        intent.putExtra("json_cart_list", json)
                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(this,"You don't have any products in Cart",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        calTotalPrice(iduser)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun initUI() {
        toolbar=findViewById(R.id.tool_bar_cart)
        rcv_cart=findViewById(R.id.rcv_cart)
        tv_total_price=findViewById(R.id.tv_total_price)
        btn_checkout=findViewById(R.id.btn_checkout)
    }
    fun calTotalPrice(idUser:String){
        var total_price:Long = 0
        firebaseService.getCartList(idUser){
            for (i in 0 until it.size){
                total_price += it[i].totalcost!!

            }
            Log.i("test",it.toString())
            val dcf = DecimalFormat("#,###")
            val dcfs = DecimalFormatSymbols(Locale.getDefault())
            dcfs.groupingSeparator='.'
            dcf.decimalFormatSymbols=dcfs
            Log.i("test",dcf.format(total_price) + " đ")
            tv_total_price.text =dcf.format(total_price) + " đ"
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu= menuInflater.inflate(R.menu.toolbar_cart,menu)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder) {
        if(viewHolder is CartAdapter.ViewHolder){
            var nameCartDel:String= arrayCart.get(viewHolder.adapterPosition).id!!
            val cartDel: Cart =arrayCart.get(viewHolder.adapterPosition)
            val indexDelete = viewHolder.adapterPosition
            cartAdapter.removeItem(indexDelete)

        }
    }
}