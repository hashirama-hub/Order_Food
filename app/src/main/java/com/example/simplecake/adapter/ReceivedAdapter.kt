package com.example.simplecake.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.Activity.Cake_Details_Activity
import com.example.simplecake.R
import com.example.simplecake.model.Order
import com.example.simplecake.service.FirebaseService
import com.google.gson.Gson

class ReceivedAdapter(var context: Context,var listWaiting:ArrayList<Order>):RecyclerView.Adapter<ReceivedAdapter.ViewHolder>() {
    var firebaseService=FirebaseService()
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tv_day_receive=view.findViewById<TextView>(R.id.tv_day_receive)
        val rcv_order=view.findViewById<RecyclerView>(R.id.rcv_order)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recived,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_day_receive.text="Received of the day: "+listWaiting[position].dateOder
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        holder.rcv_order.layoutManager=linearLayoutManager
        val checkoutAdapter= CheckoutAdapter(context, listWaiting[position].listIdCart!!)
        holder.rcv_order.adapter=checkoutAdapter
        checkoutAdapter.onItemClick={cart->
            firebaseService.getCake(cart.idCake){cake->
                val intent = Intent(context, Cake_Details_Activity::class.java)
                val gson = Gson()
                val json = gson.toJson(cake)
                intent.putExtra("json_cake",json)
                ContextCompat.startActivity(context, intent, null)
            }
        }
    }
    fun removeItem(index:Int){
        firebaseService.delOrder(listWaiting[index].id){
            if(it==true){
                Toast.makeText(context,"Delete Cart Successfully",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(context,"Delete Cart Failed",Toast.LENGTH_SHORT).show()
            }
        }
        listWaiting.removeAt(index)
        notifyItemRemoved(index)
    }
    override fun getItemCount(): Int {
        return listWaiting.size
    }
}