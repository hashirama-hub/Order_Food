package com.example.simplecake.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simplecake.*
import com.example.simplecake.Activity.ListCakeAtivity
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.adapter.ListCategoryFragmentAdapter
import com.example.simplecake.model.category1
import com.example.simplecake.service.FirebaseService
import com.google.gson.Gson
import java.util.ArrayList


class ListCategoryFragment : Fragment() {
    lateinit var mView: View
    lateinit var mMainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val firebaseService = FirebaseService()
        mView= inflater.inflate(R.layout.fragment_list_category, container, false)
        mMainActivity = activity as MainActivity
        val rcv_catelist1 = mView.findViewById<RecyclerView>(R.id.rcv_listCate)
//        val listCategoryFragmentAdapter = ListCategoryFragmentAdapter(mMainActivity,getListCategories())
        val layoutManager:LinearLayoutManager = LinearLayoutManager(mMainActivity)
//
//        listCategoryFragmentAdapter.onItemClick={
//            val intent = Intent(mMainActivity,ListCakeAtivity::class.java)
//            val gson = Gson()
//            val json = gson.toJson(it)
//            intent.putExtra("json_name_category",json)
//            startActivity(intent)
//        }
        val allCategory: ArrayList<category1> = ArrayList()
        firebaseService.getCategories{ arrayListCategory->
            arrayListCategory

//            val listCategoryApdapter = ListCategoryApdapter(mMainActivity,arrayListStopPointInfo)
            val listCategoryFragmentAdapter = ListCategoryFragmentAdapter(mMainActivity,arrayListCategory)
            rcv_catelist1.adapter = listCategoryFragmentAdapter
            listCategoryFragmentAdapter.onItemClick={
                val intent = Intent(mMainActivity, ListCakeAtivity::class.java)
                val gson = Gson()
                val json = gson.toJson(it)
                intent.putExtra("json_name_category",json)
                startActivity(intent)
            }
        }

        rcv_catelist1.layoutManager=layoutManager
        val scrollView=mView.findViewById<ScrollView>(R.id.scrollview)
        scrollView.isSmoothScrollingEnabled = true
        scrollView.setOnTouchListener(TranslateAnimationUtil(mMainActivity, mMainActivity.bottomNavigation))
        return mView
    }

  ;

}