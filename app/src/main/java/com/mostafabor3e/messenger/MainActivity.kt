package com.mostafabor3e.messenger

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mostafabor3e.messenger.Fragment.Chat
import com.mostafabor3e.messenger.Fragment.Discover
import com.mostafabor3e.messenger.Fragment.People
import com.mostafabor3e.messenger.Model.GlideApp
import com.mostafabor3e.messenger.Model.User
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.iid.FirebaseInstanceIdService as FirebaseInstanceIdService1

class MainActivity : AppCompatActivity(),View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private val chat= Chat()
    private  val people= People()
    private  val  discover= Discover()
    private val firebaseAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firebaseInstant:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val firebaseStorage:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
   // val id="Gz9N5e3Mr9R9oEHJi5L5atLGKEj2"
    val id=firebaseAuth.currentUser?.uid.toString()
    private val reference: DocumentReference
        get()=firebaseInstant.document("Users/${id}")



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toast.makeText(this,"id"+id,Toast.LENGTH_SHORT).show()




        reference.get().addOnSuccessListener {
                if (it.data!=null){
                    val user=it.toObject(User::class.java)
                    if (user!!.profile_image.isNotEmpty()){
                        GlideApp.with(this)
                            .load(firebaseStorage.getReference(user.profile_image)).
                            placeholder(R.drawable.masslog)
                            .into(iv_profile_main)
                    }
                    else{
                        iv_profile_main.setImageResource(R.drawable.masslog)
                    }}
        setSupportActionBar(toolbar)

        val ac=supportActionBar
        ac?.title=""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }else{
            window.statusBarColor=Color.WHITE
        }

        log.setOnClickListener(this)
        iv_profile_main.setOnClickListener(this)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@MainActivity)
        setFragment(chat)
val to=FirebaseInstanceId.getInstance().token
//Toast.makeText(this,to,Toast.LENGTH_LONG).show()

            }


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.log->{
                firebaseAuth.signOut()
                val intent:Intent= Intent(this,login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            R.id.iv_profile_main->{
                val intent:Intent= Intent(this,Profile::class.java)
                startActivity(intent)
            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_chat -> {
                setFragment(chat)
                return true
            }
            R.id.item_people -> {
                setFragment(people)
                return true
            }
            R.id.item_discover -> {
                setFragment(discover)
                return true
            }
            else->
                return false

        }

    }

private fun setFragment(frag:Fragment){
    val y=supportFragmentManager.beginTransaction()
    y.replace(R.id.coordint,frag)
    y.commit()
}
}