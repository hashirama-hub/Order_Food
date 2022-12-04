package com.example.simplecake.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simplecake.*
import com.example.simplecake.Activity.Cake_Details_Activity
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.Activity.OrderActivity
import com.example.simplecake.adapter.FavoriteAdapter
import com.example.simplecake.model.Cake1
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson

class MeFragment : Fragment() {
    lateinit var mView:View
    var mainActivity= MainActivity()
    lateinit var img_avatar_me:ImageView
    lateinit var tv_name_me:TextView
    lateinit var tv_email_me:TextView
    lateinit var rcv_cake:RecyclerView
    var firebaseService=FirebaseService()
    lateinit var tv_waiting:TextView
    lateinit var tv_recived:TextView
    lateinit var tv_rating:TextView
    lateinit var tv_cacled:TextView
    lateinit var listCake:ArrayList<Cake1>
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_me, container, false)
        mainActivity = activity as MainActivity
        initUI()
        firebaseService.getListFavorite(user!!.uid){listFavorite ->
            val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            rcv_cake.layoutManager = linearLayoutManager
            val cakeAdapter = FavoriteAdapter(mainActivity,listFavorite)
            rcv_cake.adapter=cakeAdapter
            cakeAdapter.onItemClick={
                firebaseService.getCake(it.idCake){cake->
                            val intent = Intent(context, Cake_Details_Activity::class.java)
                            val gson = Gson()
                            val json = gson.toJson(cake)
                            intent.putExtra("json_cake",json)
                            ContextCompat.startActivity(mainActivity, intent, null)

                }
            }

        }
        tv_waiting.setOnClickListener {
            val intent= Intent(mainActivity, OrderActivity::class.java)
            startActivity(intent)
        }
        setUserInformation()
        return mView

    }
    fun initUI(){
        img_avatar_me=mView.findViewById(R.id.img_avatar_me)
        tv_name_me=mView.findViewById(R.id.tv_name_me)
        tv_email_me=mView.findViewById(R.id.tv_email_me)
        rcv_cake=mView.findViewById(R.id.rcv_cakes)
        tv_waiting=mView.findViewById(R.id.tv_waiting)
        tv_recived=mView.findViewById(R.id.tv_received)
        tv_cacled=mView.findViewById(R.id.tv_cancel)
        tv_rating=mView.findViewById(R.id.tv_rate)
    }
    private fun setUserInformation() {

        if(user == null){
            return
        }
        tv_name_me.setText(user.displayName)
        tv_email_me.setText(user.email)
        val photoUrl = user.photoUrl
        Log.i("test",photoUrl.toString())
        Glide.with(this).load(photoUrl).error(R.drawable.default_user).into(img_avatar_me)
    }


}