package com.example.simplecake.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.simplecake.Activity.ProductActivity

import com.example.simplecake.R
import com.example.simplecake.adapter.ProductAdminAdapter
import com.example.simplecake.service.FirebaseService


class ListProductFragment : Fragment() {
    lateinit var mView: View
    lateinit var rcv_products:RecyclerView

    var productActivity=ProductActivity()
    var firebaseService=FirebaseService()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_list_product, container, false)
        initUI()
        productActivity = activity as ProductActivity
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(productActivity,RecyclerView.VERTICAL,false)
        rcv_products.layoutManager = linearLayoutManager
        firebaseService.getFullListCake{ listCake ->
            Log.i("test",listCake.toString())
            var productAdminAdapter  = ProductAdminAdapter(productActivity,listCake)
            rcv_products.adapter=productAdminAdapter
        }
        return mView
    }
    fun initUI(){
        rcv_products=mView.findViewById(R.id.rcv_products)
    }

}