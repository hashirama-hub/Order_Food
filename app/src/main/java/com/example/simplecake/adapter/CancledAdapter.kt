package com.example.simplecake.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.R
import com.example.simplecake.model.Order
import com.example.simplecake.service.FirebaseService
import java.sql.Timestamp
import java.text.SimpleDateFormat

class CancledAdapter(var context: Context,var listWaiting:ArrayList<Order>):RecyclerView.Adapter<CancledAdapter.ViewHolder>() {
    var firebaseService=FirebaseService()
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val tv_day_receive=view.findViewById<TextView>(R.id.tv_day_receive)
        val rcv_order=view.findViewById<RecyclerView>(R.id.rcv_order)
        val btn_confirm=view.findViewById<Button>(R.id.btn_confirm)
        val tv_phone=view.findViewById<TextView>(R.id.tv_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_canceled,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_day_receive.text="Order of the day: "+listWaiting[position].dateOder
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
         holder.rcv_order.layoutManager=linearLayoutManager
        holder.rcv_order.adapter= CheckoutAdapter(context, listWaiting[position].listIdCart!!)
        holder.tv_phone.text = "Tel: "+listWaiting[position].phoneNumber
        holder.btn_confirm.setOnClickListener {
            val sdf = SimpleDateFormat("yyyy.MM.dd")
            val timestamp = Timestamp(System.currentTimeMillis())
            var today=sdf.format(timestamp)
            Log.i("test",listWaiting[position].id.toString())
            firebaseService.updateDateCancle(listWaiting[position].id.toString(),today.toString()){

            }
            firebaseService.getCanceled(listWaiting[position].id) { cancled ->
                firebaseService.insertOrder(cancled) {
                    if (it == true) {
                        Toast.makeText(context, "Ordered Successfully", Toast.LENGTH_SHORT).show()
                        removeItem(position)
                    } else {
                        Toast.makeText(context, "Can't order this product", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }
    fun removeItem(index:Int){
        firebaseService.delCancel(listWaiting[index].id){
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