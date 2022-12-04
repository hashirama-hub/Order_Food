package com.example.simplecake.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.Activity.MainActivity.Companion.MY_REQUEST_CODE
import com.example.simplecake.R
import com.example.simplecake.model.User
import com.example.simplecake.service.FirebaseService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mView:View
    lateinit var imgAvatar:ImageView
    lateinit var et_name:EditText
    var mUri: Uri? =null
    lateinit var et_email:EditText
    lateinit var btn_update:Button
    lateinit var mMainActivity: MainActivity
    lateinit var progressDialog: ProgressDialog
    lateinit var btnUpdateEmail:Button
    lateinit var et_phone:EditText
    var firebaseService = FirebaseService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mMainActivity = activity as MainActivity
        progressDialog = ProgressDialog(activity)
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false)
        initUI()
        setUserInformation()
        initListener()
        return mView
    }

    private fun initListener() {
        imgAvatar.setOnClickListener {
            onClickRequestPermission()
        }
        btn_update.setOnClickListener {
            onClickUpdateProfile()
        }
        btnUpdateEmail.setOnClickListener {
            if(et_email.text.toString()!=null){
                openVerifyAccount()
            }
            else{
                Toast.makeText(activity,"You must import your Email!!",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onClickUpdateEmail() {
        progressDialog.show()

        var email:String = et_email.text.toString().trim()
        var user:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        user!!.updateEmail(email)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                  Toast.makeText(activity,"Update Email Completed",Toast.LENGTH_SHORT).show()
                    mMainActivity.showUserInformation()
                }
                else{
                    Toast.makeText(activity,"Update Email Failed",Toast.LENGTH_SHORT).show()
                }
            }

    }
    public fun setUri(uri: Uri?){
        if (uri != null) {
            this.mUri=uri
        }
    }
    private fun onClickUpdateProfile() {
        val user = Firebase.auth.currentUser ?: return
        progressDialog.show()
        var strName:String = et_name.text.toString().trim()
        var strEmail:String = et_email.text.toString().trim()
        var strTelNum:String = et_phone.text.toString().trim()
        val profileUpdates = userProfileChangeRequest {
            displayName = strName
            photoUri = if (mUri==null) {
                Uri.parse(user.photoUrl.toString())

            }else{
                Uri.parse(mUri.toString())
            }
            val userUpdate = User(user.uid,photoUri.toString(),"user",strEmail,strName,strTelNum)
            firebaseService.insertUser(userUpdate){

            }
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(activity,"Update Succesfully",Toast.LENGTH_SHORT).show()
                    mMainActivity.showUserInformation()
                }
            }
    }

    private fun onClickRequestPermission() {
        mMainActivity = activity as MainActivity
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mMainActivity.openGallery()
            return
        }
        if (activity?.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mMainActivity.openGallery()
        }else{
            var permission:Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            activity?.requestPermissions(permission,MY_REQUEST_CODE)
        }
    }


    private fun setUserInformation() {
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(user == null){
            return
        }
        et_name.setText(user.displayName)
        et_email.setText(user.email)
        firebaseService.getUser(user.uid){
            et_phone.setText(it.phone, TextView.BufferType.EDITABLE)
        }
        val photoUri = Uri.parse(user.photoUrl.toString())
        Glide.with(mMainActivity).load(photoUri).error(R.drawable.default_user).into(imgAvatar)
    }

    private fun initUI(){
        imgAvatar = mView.findViewById(R.id.img_avatar_profile)
        et_email=mView.findViewById(R.id.et_email_update)
        et_name=mView.findViewById(R.id.et_name)
        et_phone=mView.findViewById(R.id.et_phone)
        btn_update=mView.findViewById(R.id.btn_update_profile)
        btnUpdateEmail = mView.findViewById(R.id.btn_update_email)
    }
    public fun setBitMapImageView(bitmapImageView: Bitmap){
        imgAvatar.setImageBitmap(bitmapImageView)
    }


    private fun ReAuthetication(strEmail: String,strPassword:String){
        val user = Firebase.auth.currentUser!!

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential(strEmail, strPassword)

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    onClickUpdateEmail()
                }else{
                    Toast.makeText(activity,"Your password or email incorrect",Toast.LENGTH_SHORT).show()
                }

            }
    }
    private fun openVerifyAccount(){
        val view = View.inflate(activity,R.layout.layout_dialog,null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog= builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val et_verify_email = view.findViewById<EditText>(R.id.et_verify_email)
        val et_verify_password = view.findViewById<EditText>(R.id.et_verify_password)
        val btn_verify = view.findViewById<Button>(R.id.btn_verify)
        val btn_cancel = view.findViewById<Button>(R.id.btn_cancle)
        btn_cancel.setOnClickListener {
            dialog.dismiss()
        }
        btn_verify.setOnClickListener {
            ReAuthetication(et_verify_email.text.toString(),et_verify_password.text.toString())
            dialog.dismiss()
        }
    }
}