package com.mostafabor3e.messenger.Recycle_Grop

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mostafabor3e.messenger.Model.GlideApp
import com.mostafabor3e.messenger.Model.ImageMessage
import com.mostafabor3e.messenger.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_reciver_mass.*
import kotlinx.android.synthetic.main.message_image_item.*

class Image_item(val message:ImageMessage,val id:String,val context:Context): Item() {
    private val firebaseStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val reference: StorageReference
        get()=firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())

    override fun bind(viewHolder: ViewHolder, position: Int) {
if (message.image.isNotEmpty()){
        GlideApp.with(context).load(firebaseStorage.getReference(message.image)).
        placeholder(R.drawable.masslog)
            .into(viewHolder.iv_message_Image)}

        viewHolder.tv_date_Image.text=android.text.format.DateFormat.format("hh:mm a",message.date)

    }

    override fun getLayout(): Int {
        return R.layout.message_image_item
    }
}