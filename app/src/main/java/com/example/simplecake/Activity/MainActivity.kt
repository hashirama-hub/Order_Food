package com.example.simplecake.Activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.simplecake.R
import com.example.simplecake.fragment.*
import com.example.simplecake.service.FirebaseService
import com.google.android.gms.tasks.OnCompleteListener

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
//    companion object{
//        const val TAG:String = MainActivity::class.java.name
//    }
companion object{
    const val FRAGMENT_HOME:Int=0
    const val FRAGMENT_FAVORITE:Int=1
    const val FRAGMENT_HISTORY:Int=2
   const val FRAGMENT_MY_PROFILE:Int=3
    const val FRAGMENT_CHANGE_PASSWORD:Int=4
    const val FRAGMENT_ME:Int=5
    const val FRAGMENT_CAKE:Int=6
    const val MY_REQUEST_CODE:Int=10
}
    var currentFragment:Int = FRAGMENT_HOME
    var myProfileFragment:MyProfileFragment = MyProfileFragment()
    lateinit var navigationView:NavigationView
    lateinit var DrawerLayout:DrawerLayout
    lateinit var bottomNavigation:BottomNavigationView
    lateinit var imgAvatar:ImageView
    lateinit var tv_name:TextView
    lateinit var tv_email:TextView
    lateinit var tv_count:TextView
    var count_cart:Int = 0
    private var backPresssTime=0L
    lateinit var mViewPager:ViewPager
    var firebaseService=FirebaseService()
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val iduser:String = user!!.uid
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent = result.data ?: return@registerForActivityResult
               val uri: Uri? = intent.data
                myProfileFragment.setUri(uri)
               val bitmap:Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                myProfileFragment.setBitMapImageView(bitmap)

        }
    }
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //navigation view
        navigationView=findViewById(R.id.navigation_view)

        //init UI
        initUI()
        //get Push token
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Token", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.e("Token", token)
        })

        //setup toolbar
        var toolbar:androidx.appcompat.widget.Toolbar = findViewById(R.id.tool_bar)

        setSupportActionBar(toolbar)
        DrawerLayout = findViewById(R.id.drawable_layout)
        var toggle:ActionBarDrawerToggle= ActionBarDrawerToggle(this,DrawerLayout,toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        DrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //bottnavigation
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.selectedItemId  = R.id.home

        bottomNavigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.home -> {
                    replaceFragment(HomeFragment())

                }

                R.id.cake -> {
                    replaceFragment(ListCategoryFragment())
                    currentFragment= FRAGMENT_CAKE
                }
                R.id.my_account ->
                {
                    replaceFragment(MeFragment())
                    currentFragment= FRAGMENT_ME

                }
                else -> {
                    replaceFragment(HomeFragment())
                }
            }
            true
        }
        
        //endbottomnavigation

        // hidden or show navbottom

    showUserInformation()
        navigationView.setNavigationItemSelectedListener(this)
        replaceFragment(HomeFragment())
        navigationView.menu.findItem(R.id.nav_home).isChecked = true

    }

    private fun initUI(){
        imgAvatar=navigationView.getHeaderView(0).findViewById(R.id.img_avatar)
        tv_email=navigationView.getHeaderView(0).findViewById(R.id.tv_email)
        tv_name=navigationView.getHeaderView(0).findViewById(R.id.tv_my_name)

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
                tv_name.visibility=View.GONE
            }else{
                tv_name.visibility=View.VISIBLE
                tv_name.text = name
            }
            tv_email.text=email
            Glide.with(this).load(photoUrl).error(R.drawable.default_user).into(imgAvatar)
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar,menu)
//        val menuItem: MenuItem? = menu!!.findItem(R.id.cart)
//
//        menuItem?.setActionView(R.layout.custom_notifcation_layout)
//        val view:View= menuItem!!.actionView
//        tv_count=view.findViewById(R.id.notification_count)
//        val frame_cart = view.findViewById<FrameLayout>(R.id.frame_cart)
//        frame_cart.setOnClickListener {
//            val intent = Intent(this,CartActivity::class.java)
//            startActivity(intent)
//        }
//        count_cart=0
//        firebaseService.getCartList(iduser) {
//            for (i in 0 until it.size) {
//                count_cart += it[i].qualities!!
//            }
//            tv_count.text = count_cart.toString()
//        }

        return true
    }


    //auto load silde image

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if(id== R.id.nav_home){
            if(currentFragment!= FRAGMENT_HOME){
                replaceFragment(HomeFragment())
                currentFragment= FRAGMENT_HOME
            }
        }
        else if(id== R.id.nav_favorite){
            if(currentFragment!= FRAGMENT_FAVORITE){
                replaceFragment(FavoriteFragment())
                currentFragment= FRAGMENT_FAVORITE
            }
        }
        else if(id== R.id.nav_history){
            if(currentFragment!= FRAGMENT_HISTORY){
                replaceFragment(HistoryFragment())
                currentFragment= FRAGMENT_HISTORY
            }
        }
        else if(id== R.id.nav_signout){
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(id== R.id.nav_my_profile){
            if(currentFragment!= FRAGMENT_MY_PROFILE){
                replaceFragment(myProfileFragment)
                currentFragment= FRAGMENT_MY_PROFILE
            }
        }
        else if(id== R.id.nav_change_password){
            if(currentFragment!= FRAGMENT_CHANGE_PASSWORD){
                replaceFragment(ChangePasswordFragment())
                currentFragment= FRAGMENT_CHANGE_PASSWORD
            }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openGallery()
            }
        }
    }

    public fun openGallery() {
         val intent = Intent()
        intent.type = "image/*"
        intent.action=Intent.ACTION_GET_CONTENT
        startForResult.launch(Intent.createChooser(intent,"Select Picture"))

    }

    private fun replaceFragment(fragment:Fragment){
        var transaction:FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout,fragment)
        transaction.commit()
    }
}
