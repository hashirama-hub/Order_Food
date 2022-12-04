package com.example.simplecake.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.simplecake.R
import com.example.simplecake.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class OrderActivity : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var mViewpager:ViewPager
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        initUI()
        setSupportActionBar(toolbar)
        toolbar.title="Order"
        val viewPagerAdapter= ViewPagerAdapter(supportFragmentManager,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        mViewpager.adapter=viewPagerAdapter
        tabLayout.setupWithViewPager(mViewpager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun initUI(){
        toolbar=findViewById(R.id.tool_bar_order)
        tabLayout=findViewById(R.id.tab_layout)
        mViewpager=findViewById(R.id.viewPagerOrder)

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menu= menuInflater.inflate(R.menu.toolbar_cart,menu)
        return true
    }

}