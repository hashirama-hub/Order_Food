package com.example.simplecake.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.adapter.CancledAdapter
import com.example.simplecake.Activity.OrderActivity
import com.example.simplecake.R
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class CancledFragment : Fragment() {
    lateinit var rcv_canceled: RecyclerView
    lateinit var mview:View
    var orderActivity= OrderActivity()
    var firebaseService= FirebaseService()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mview= inflater.inflate(R.layout.fragment_cancled, container, false)
        initUI()
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid
        orderActivity=activity as OrderActivity
        firebaseService.getListCanceled(iduser){listCanceled->
            val cancledAdapter= CancledAdapter(orderActivity, listCanceled)
            rcv_canceled.adapter=cancledAdapter

        }
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(orderActivity,RecyclerView.VERTICAL,false)
        rcv_canceled.layoutManager=linearLayoutManager
        return mview
    }
    fun initUI(){
        rcv_canceled=mview.findViewById(R.id.rcv_canceled)
    }

}