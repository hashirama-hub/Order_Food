package com.example.simplecake.fragment

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.simplecake.Activity.MainActivity
import com.example.simplecake.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePasswordFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mView:View
    lateinit var btn_change_pass:Button
    lateinit var et_new_pass:EditText
    lateinit var et_confirm_new_pass:EditText
    lateinit var progressDialog:ProgressDialog
    var mMainActivity: MainActivity = MainActivity()
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
        mView= inflater.inflate(R.layout.fragment_change_password, container, false)
        // Inflate the layout for this fragment
        progressDialog=ProgressDialog(activity)
        initUI()
        btn_change_pass.setOnClickListener {
//            onClickChangePassword()
            if(et_confirm_new_pass.text.toString()==et_new_pass.text.toString()) {
                openVerifyAccount()
            }
            else{
                Toast.makeText(activity,"Your new password and confirm new password are different!!!",Toast.LENGTH_LONG).show()
            }
        }

        return mView
    }

    private fun onClickChangePassword() {
        var strPassword:String = et_new_pass.text.toString().trim()
        progressDialog.show()
        val user = Firebase.auth.currentUser
        val newPassword = strPassword

        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                if (task.isSuccessful) {
                    Toast.makeText(activity,"User password updated.",Toast.LENGTH_SHORT).show()
                }
                else{
                    //show Dialog

                }
            }
    }

    private fun initUI(){
        btn_change_pass=mView.findViewById(R.id.btn_change_password)
        et_new_pass=mView.findViewById(R.id.et_new_password)
        et_confirm_new_pass=mView.findViewById(R.id.et_confirm_new_password)
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
                    onClickChangePassword()
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangePasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}