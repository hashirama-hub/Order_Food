package com.example.simplecake.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.simplecake.R
import com.example.simplecake.model.User
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.FirebaseAuth

class RegistorActivity : AppCompatActivity() {
    lateinit var et_email:EditText
    lateinit var et_password:EditText
    lateinit var et_re_password:EditText
    lateinit var btn_registor:Button
    lateinit var progressDialog:ProgressDialog
    var firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registor)
        initUI()
        initListener()
    }

    private fun initListener() {
        btn_registor.setOnClickListener {
            if(et_re_password.text.toString()==et_password.text.toString()) {
                onClickSignUp()
            }
            else{
                Toast.makeText(this,"Your password and confirm password are different!!",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onClickSignUp() {
        val email:String = et_email.text.toString().trim()
        val password:String = et_password.text.toString().trim()
        val auth:FirebaseAuth = FirebaseAuth.getInstance()
        progressDialog.show()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss()
                val user = User(auth.uid,"","user", email,"","")
                firebaseService.insertUser(user){

                }
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

//        firebaseService.insertUser()
    }

    private fun initUI(){
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_re_password = findViewById(R.id.et_re_password)
        btn_registor = findViewById(R.id.btn_register)
        progressDialog = ProgressDialog(this)
    }
}