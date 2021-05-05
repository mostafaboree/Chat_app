package com.mostafabor3e.messenger

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mostafabor3e.messenger.Model.GlideApp
import com.mostafabor3e.messenger.Model.User
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.util.*

class Profile : AppCompatActivity(),View.OnClickListener {
    val RequestcodeIMAGE = 0
    val TAG=MainActivity()
    private val userid=FirebaseAuth.getInstance().currentUser?.uid.toString()
    private lateinit var username:String

    private val fbs:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val firebaseAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val id=firebaseAuth.currentUser?.uid.toString()
    private val reference: DocumentReference
        get()=fbs.document("Users/${id}")

    private  val storageref:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val storage:StorageReference
        get()=storageref.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar_profile)






        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }
        bt_back.setOnClickListener(this)
        iv_profile_profile.setOnClickListener(this)
        userInf {
            user ->
            username=user.name
            Toast.makeText(this,"username"+user.profile_image,Toast.LENGTH_SHORT).show()
            if (user.profile_image.isNotEmpty()){
          GlideApp.with(this).load(storageref.getReference(user.profile_image)).
              placeholder(R.drawable.masslog).
          into(iv_profile_profile)}



        }



    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_back -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.iv_profile_profile -> {
                val intent: Intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(Intent.createChooser(intent,"Select Image"), RequestcodeIMAGE)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==RequestcodeIMAGE && resultCode== Activity.RESULT_OK&&data!=null){
            //convert image to bit

            val selectimage=data.data
            val SelectBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,selectimage)
            val  outputstream= ByteArrayOutputStream()
            SelectBitmap.compress(Bitmap.CompressFormat.JPEG,20,outputstream)
            val selectImageToBitye=outputstream.toByteArray()
            uploadprofileimage(selectImageToBitye){
                val m= mutableMapOf<String,Any>()
                m["name"]=username
                m["profile_image"]=it
                reference.update(m)
               // iv_profile_profile.setImageURI(Uri.parse(path))
            }


        }
    }

    private fun uploadprofileimage(selectImageToBitye: ByteArray,onsuccess:(imagePath:String)->Unit) {
        val progress=ProgressDialog(this)
        progress.setMessage("uploading Image")
        progress.show()
       val ref=storage.child("profile_image/${UUID.nameUUIDFromBytes(selectImageToBitye)}")
        ref.putBytes(selectImageToBitye).addOnCompleteListener(){
            if(it.isSuccessful){
                progress.dismiss()

                    onsuccess(ref.path)
                Toast.makeText(this,"profile_image_change",Toast.LENGTH_SHORT).show()
                }

               


            else{
                Toast.makeText(this,"profile_image_no_change"+it.exception,Toast.LENGTH_SHORT).show()


            }
        }
    }


    private fun userInf(oncomplete:(User)->Unit){

        reference.get().addOnSuccessListener {
            if(it.data!=null){
            oncomplete(it.toObject(User::class.java)!!)
        }}

        }

    }

