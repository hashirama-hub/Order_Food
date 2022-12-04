package com.example.simplecake.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.fragment.HomeFragment

import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdminActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var navigationView: NavigationView
    lateinit var DrawerLayout: DrawerLayout
    lateinit var imgAvatar: ImageView
    lateinit var tv_name: TextView
    lateinit var tv_email: TextView
    lateinit var cardView_products:CardView
    private var backPresssTime=0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        initUI()
        //setup toolbar
        var toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.tool_bar)

        setSupportActionBar(toolbar)
        DrawerLayout = findViewById(R.id.drawable_layout)
        var toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this,DrawerLayout,toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        cardView_products.setOnClickListener {
            val intent = Intent(this,ProductActivity::class.java)
            startActivity(intent)
        }
        DrawerLayout.addDrawerListener(toggle)
        showUserInformation()
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        //

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    public fun showUserInformation(){
        var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user==null){
            return
        }
        else{
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl
            if(name==null){
                tv_name.visibility= View.GONE
            }else{
                tv_name.visibility= View.VISIBLE
                tv_name.text = name
            }
            tv_email.text=email
            Glide.with(this).load(photoUrl).error(R.drawable.default_user).into(imgAvatar)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if(id== R.id.nav_signout){
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }
        DrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {

        if(backPresssTime + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed()
        }else{
            Toast.makeText(applicationContext,"Press back again to exit Application",Toast.LENGTH_SHORT).show()
        }
        backPresssTime=System.currentTimeMillis()
//        if(DrawerLayout.isDrawerOpen(GravityCompat.START)){
//            DrawerLayout.closeDrawer(GravityCompat.START)
//        }else{
//            super.onBackPressed()
//        }
    }
    fun initUI(){
        navigationView=findViewById(R.id.navigation_view)
        imgAvatar=navigationView.getHeaderView(0).findViewById(R.id.img_avatar)
        tv_email=navigationView.getHeaderView(0).findViewById(R.id.tv_email)
        tv_name=navigationView.getHeaderView(0).findViewById(R.id.tv_my_name)
        DrawerLayout = findViewById(R.id.drawable_layout)
        cardView_products= findViewById(R.id.cardView_products)
    }
}