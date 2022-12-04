package com.example.simplecake.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.simplecake.fragment.*

class ViewPagerAdminAdapter(fm: FragmentManager, behavior:Int):
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0->return ListProductFragment()
            1->return InsertProductFragment()
            else->return ListProductFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title:String =""
        when(position){
            0->title="List Product"
            1->title="Add Product"


        }
        return title
    }
}