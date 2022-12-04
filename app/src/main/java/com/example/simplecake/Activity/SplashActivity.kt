package com.example.simplecake.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.simplecake.R
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {
    private var backPresssTime=0L
    var firebaseService=FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//            var handle:Handler
            Handler().postDelayed({
                 nextActivity()

            }, 2000)

        }
    override fun onBackPressed() {
        if(backPresssTime + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed()
        }else{
            Toast.makeText(applicationContext,"Press back again to exit Application", Toast.LENGTH_SHORT).show()
        }
        backPresssTime=System.currentTimeMillis()
    }
    private fun nextActivity() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user==null){
            //chua login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }else{
            //da login
            firebaseService.getUser(user.uid){
                if (it.decentralization.equals("admin")){
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        }
//        finish()
    }
}
