package com.example.simplecake.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.simplecake.fragment.*

class ViewPagerAdapter(fm: FragmentManager, behavior:Int):
    FragmentStatePagerAdapter(fm, behavior) {
    override fun getCount(): Int {
        return 4
    }

    override fun getItem(position: Int): Fragment {
        when(position){
            0->return WaitingFragment()
            1->return ReciviedFragment()
            2->return ReciviedFragment()
            3->return CancledFragment()
            else->return WaitingFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title:String =""
        when(position){
            0->title="Waiting"
            1->title="Received"
            2->title="Rating"
            3->title="Cancled"
        }
        return title
    }
}