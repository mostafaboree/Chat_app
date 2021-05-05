package com.mostafabor3e.messenger.Recycle_Grop

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mostafabor3e.messenger.Model.GlideApp
import com.mostafabor3e.messenger.Model.GlideApp.get
import com.mostafabor3e.messenger.Model.User
import com.mostafabor3e.messenger.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_item.*

class ChatItem( val user:User,val uid:String, val context:Context) : Item() {
    private val firebaseFirestore:FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val firebaseStorage:FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }


     private val reference: DocumentReference
        get()=firebaseFirestore.document("Users/$uid")


    override fun bind(viewHolder: ViewHolder, position: Int) {
        getcurrentUser { user ->
            viewHolder.tv_item_massege.text="..last massage....."
            viewHolder.tv_item_name.text=user.name
            viewHolder.tv_item_time.text="9:04"
            if (user.profile_image.isNotEmpty()){
                GlideApp.with(context).load(firebaseStorage.getReference(user.profile_image))
                    .into(viewHolder.iv_item_user)
            } else{
                viewHolder.iv_item_user.setImageResource(R.drawable.masslog)
            }

        }
    }

   private fun getcurrentUser(onsucces:(User)->Unit) {

           reference.get().addOnSuccessListener { user ->
               if (user.data!=null) {
                   onsucces(user.toObject(User::class.java)!!)
               }}.addOnFailureListener { e ->
               Log.d("Error User", e.message!!)       }

    }
    override fun getLayout(): Int {
        return R.layout.recycler_view_item


    }
}