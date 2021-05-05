package com.mostafabor3e.messenger

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mostafabor3e.messenger.Model.User
import kotlinx.android.synthetic.main.activity_singin.*
import java.util.regex.Pattern

class singin : AppCompatActivity() ,TextWatcher{
    var user:FirebaseAuth?=null

    private  val db:FirebaseFirestore by  lazy {
        FirebaseFirestore.getInstance()
    }
    private val referanc:DocumentReference
        get() =db.document("Users/${user?.currentUser?.uid.toString()}")



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val  y=supportActionBar
        y?.hide()
        setContentView(R.layout.activity_singin)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //  this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        login_name.addTextChangedListener(this)
        edit_password_singIn.addTextChangedListener(this)
        email.addTextChangedListener(this)
        user= FirebaseAuth.getInstance()



bt_sing.setOnClickListener {

    val name=login_name.text?.trim().toString()
    val Email=email.text?.trim().toString()
    val password=edit_password_singIn.text?.trim().toString()
    if (name.isEmpty()){
        login_name.error="Name Required"
        login_name.requestFocus()
        return@setOnClickListener
    }
    if (Email.isEmpty()){
        email.error="Email Required"
        email.requestFocus()
        return@setOnClickListener
    }
    if (password.isEmpty()){
        edit_password_singIn.error="Password Required"
        edit_password_singIn.requestFocus()
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
        email.error=" valid Email must content @"
        email.requestFocus()
        return@setOnClickListener
    }
    if (password.length<6){
        edit_password_singIn.error="Password less 6 character"
        edit_password_singIn.requestFocus()
        return@setOnClickListener
    }
    create_user()

}


    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        bt_sing.isEnabled=login_name.text!!.trim().isNotEmpty()
                &&email.text!!.trim().isNotEmpty()
                &&edit_password_singIn.text!!.trim().isNotEmpty()
    }


    private fun create_user(){
        val user1=User(login_name.text?.trim().toString(),"")
        user?.createUserWithEmailAndPassword(email.text.toString(),edit_password_singIn.text.toString())
            ?.addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    referanc.set(user1)
                    val intent=Intent(this,MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)

                }
                else{
                    Toast.makeText(this,"Invalid Password or Invalid Email",Toast.LENGTH_SHORT).show()


                }
            }



            }





}