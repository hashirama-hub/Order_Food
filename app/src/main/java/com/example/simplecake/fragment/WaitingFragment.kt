package com.example.simplecake.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.Activity.OrderActivity
import com.example.simplecake.R
import com.example.simplecake.adapter.WaitingAdapter
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class WaitingFragment : Fragment() {
    lateinit var rcv_waiting:RecyclerView
    lateinit var mview:View
    var orderActivity= OrderActivity()
    var firebaseService=FirebaseService()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mview=inflater.inflate(R.layout.fragment_waiting, container, false)
        initUI()
        orderActivity=activity as OrderActivity
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid

        firebaseService.getListOrder(iduser){listWaiting->

            Log.i("test",listWaiting.toString())
            val waitingAdapter= WaitingAdapter(orderActivity, listWaiting )
            rcv_waiting.adapter=waitingAdapter

        }
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(orderActivity,RecyclerView.VERTICAL,false)
        rcv_waiting.layoutManager=linearLayoutManager
        return mview
    }
    fun initUI(){
        rcv_waiting=mview.findViewById(R.id.rcv_waiting)
    }

}