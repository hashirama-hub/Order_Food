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
import com.example.simplecake.adapter.ReceivedAdapter
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ReciviedFragment : Fragment() {

    lateinit var rcv_waiting: RecyclerView
    var firebaseService= FirebaseService()
    var orderActivity= OrderActivity()
    lateinit var mview:View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mview= inflater.inflate(R.layout.fragment_recivied, container, false)

        initUI()
        orderActivity=activity as OrderActivity
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val iduser:String = user!!.uid

        firebaseService.getListReceive(iduser){listWaiting->

            Log.i("test",listWaiting.toString())
            val waitingAdapter= ReceivedAdapter(orderActivity, listWaiting )
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
