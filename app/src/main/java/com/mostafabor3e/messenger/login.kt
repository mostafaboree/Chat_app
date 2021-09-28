package com.mostafabor3e.messenger

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_singin.*

class login : AppCompatActivity(),View.OnClickListener,TextWatcher {

    private  val Auth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val  firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id=Auth.currentUser

        if(id != null){
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val a=supportActionBar
        a?.hide()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        setContentView(R.layout.activity_login)

        var Email=ed_login_email.text.trim().toString()
       var password=ed_login_password.text.trim().toString()

        bt_create_new.setOnClickListener(this)
        bt_login.setOnClickListener(this)
        ed_login_email.addTextChangedListener(this)
        ed_login_password.addTextChangedListener(this)
        tv_forget_pass.setOnClickListener(this)




    }


    override fun onClick(p0: View?) {
        val Email=ed_login_email.text.trim().toString()
        val password=ed_login_password.text.trim().toString()
        when(p0?.id){
            R.id.bt_create_new->{
                val intent=Intent(this,singin::class.java)
                startActivity(intent)
            }
            R.id.bt_login->{
                if (Email.isEmpty()){
                    ed_login_email.error="Email Required"
                    ed_login_email.requestFocus()
                    return

                }
                if (password.isEmpty()){
                    ed_login_password.error="Password Required"
                    ed_login_password.requestFocus()
                    return
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    ed_login_email.error=" valid Email must content @gmail.com"
                    ed_login_email.requestFocus()
                    return
                }
                if (password.length<6){
                    ed_login_password.error="Password less 7 character"
                    ed_login_password.requestFocus()
                    return
                }
                logIn(Email,password)
            }
            R.id.tv_forget_pass->{
                Toast.makeText(this,"you can create new password ",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun logIn(email:String?,  pass:String?){
        Auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){
            task ->
            if (task.isSuccessful){
                val token=FirebaseInstanceId.getInstance().token
                firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).update(
                    mapOf("token" to token)).addOnSuccessListener {
                     Toast.makeText(this,"token" + token,Toast.LENGTH_LONG).show()

                }
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()


            }
            else{
                Toast.makeText(this,"Error"+task.exception,Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        bt_login.isEnabled=ed_login_email.text!!.trim().isNotEmpty() && ed_login_password.text!!.trim().isNotEmpty()
    }
}