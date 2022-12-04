package com.example.simplecake.Activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.simplecake.R
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var tv_registor:TextView
    lateinit var et_email:EditText
    lateinit var et_password:EditText
    lateinit var btn_login:Button
    lateinit var email:String
    lateinit var progressDialog:ProgressDialog
    lateinit var password:String
    lateinit var tv_forgat_password:TextView
    private var backPresssTime=0L
    var firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUI()
        initUIListener()
    }

    private fun initUIListener() {
        tv_registor.setOnClickListener {
            val intent1 = Intent(this@LoginActivity, RegistorActivity::class.java)
            startActivity(intent1)
        }
        btn_login.setOnClickListener {
             onClickLogin()
        }
        tv_forgat_password.setOnClickListener {
            openDiaLogForgotPassword()
        }
    }

    override fun onBackPressed() {
        if(backPresssTime + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed()
        }else{
            Toast.makeText(applicationContext,"Press back again to exit Application",Toast.LENGTH_SHORT).show()
        }
        backPresssTime=System.currentTimeMillis()
    }
    private fun onClickForgot(email:String) {
        progressDialog.show()
        val emailAddress = email

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(this,"Email sent",Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Email don't exist",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun openDiaLogForgotPassword(){
        val view = View.inflate(this, R.layout.layout_dialog_forgot_pass,null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        val dialog= builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val et_verify_email = view.findViewById<EditText>(R.id.et_verify_email)
        val btn_sent = view.findViewById<Button>(R.id.btn_sent)
        val btn_cancel = view.findViewById<Button>(R.id.btn_cancle)
        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        btn_sent.setOnClickListener {
            onClickForgot(et_verify_email.text.toString())
            dialog.dismiss()
        }
    }
    private fun onClickLogin() {
        val auth:FirebaseAuth = FirebaseAuth.getInstance()
        email = et_email.text.toString().trim()
        password = et_password.text.toString().trim()
        progressDialog.show()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    firebaseService.getUser(auth.uid){userInfor->
                        if (userInfor.decentralization.equals("admin")){
                            val intent = Intent(this, AdminActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        else {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun initUI(){
        progressDialog = ProgressDialog(this)
        tv_registor = findViewById(R.id.tv_registor)
        et_email=findViewById(R.id.et_email)
        et_password=findViewById(R.id.et_password)
        btn_login=findViewById(R.id.btn_login)
        tv_forgat_password=findViewById(R.id.tv_forgot_password)
    }
}