package com.mostafabor3e.messenger

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Query
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.type.Date
import com.mostafabor3e.messenger.Model.*
import com.mostafabor3e.messenger.Recycle_Grop.Image_item
import com.mostafabor3e.messenger.Recycle_Grop.Item_message_reciver
import com.mostafabor3e.messenger.Recycle_Grop.MessageItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_.*
import java.io.ByteArrayOutputStream
import java.util.*
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import com.mostafabor3e.messenger.Model.GlideApp.get
import com.mostafabor3e.messenger.Remot.*
import com.squareup.okhttp.ResponseBody
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("CAST_NEVER_SUCCEEDS")
class Chat_Activity : AppCompatActivity(),View.OnClickListener,TextWatcher{
     val PH_REC_COD=102
    lateinit var athor_id:String
    lateinit var token:String

    private val firebaseStorage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val adapterma by lazy {
        GroupAdapter<ViewHolder>() }
   private val apiserver=ApiClient.getClient().create(ApiInterface::class.java)


    lateinit var user_id:String

    private val firebaseAuth:FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    val  firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    val id=firebaseAuth.currentUser?.uid.toString()
    private val reference: DocumentReference
        get()=firebaseFirestore.document("Users/${id}")

    private val storage: StorageReference
        get()=firebaseStorage.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private val chatChannelcollectin=firebaseFirestore.collection("Chatchannel")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }else{
            window.statusBarColor= Color.WHITE
        }
        val intent=getIntent()
        val username=intent.getStringExtra("username")
        val profileimage=intent.getStringExtra("profileimage")
        athor_id= intent.getStringExtra("id").toString()
         user_id=firebaseAuth.uid.toString()
        token=intent.getStringExtra("token").toString()
        Toast.makeText(this@Chat_Activity,"success"+token.toString()+username,Toast.LENGTH_LONG).show()

        rec_message.apply {
            adapter=adapterma
        }

        tv_chat_Acticity_un.text=username


        if (profileimage != null) {
                if (profileimage.isNotEmpty()){
                GlideApp.with(this)
                    .load(firebaseStorage.getReference(profileimage)).
                    placeholder(R.drawable.masslog)
                    .into(iv_frind_chatActivity)
               }
               else{
                iv_frind_chatActivity.setImageResource(R.drawable.masslog)
               }
            }
        if (ed_chat_Activity_message.text.isEmpty()){
            bt_chat_Activity_send.visibility=View.INVISIBLE
        }
        ed_chat_Activity_message.addTextChangedListener(this)
        fb_chat_Activity_photo.setOnClickListener(this)
        iv_chatActivity_back.setOnClickListener(this)

        ChatChannel{channel->
            getMessage(channel)

            bt_chat_Activity_send.setOnClickListener{
                userInf {  user->
                val textmessage= TextMessage(
                    ed_chat_Activity_message.text.toString(),
                    user_id,user.name,athor_id,
                    Calendar.getInstance().time,
                    MessageType.Text
                )
                SendMessage(channel,textmessage)

                sendNotifaction(
                    "e3NPnhlTRZiuusrMCXYvmh:APA91bGPCAkLivfYjXFUvz2NP5RBIiFLtPNUubi3i6UDDgx6JRo" +
                            "Uig9KBKF9Igw8TpEViPc8HJS158sYC6ux8MYYH8aAy" +
                            "ZRimrJhonROMuII3hLs9PEFSPPxGrFum_2_YqvoYndSw7ek",
                    user.name,ed_chat_Activity_message.text.toString()
                )
            }}
            ed_chat_Activity_message.setText("")

        }


    }

    private fun getMessage(channel_id:String) {

        val message = chatChannelcollectin.document(channel_id)
            .collection("Message").orderBy("date",com.google.firebase.firestore.Query.Direction.DESCENDING);
        message.addSnapshotListener { query, exaption ->
            adapterma.clear()

            query!!.documents.forEach {document->

                if (document["type"] == MessageType.Text){
                    val me= document.toObject(TextMessage::class.java)

                    if ( me?.sender==user_id) {
                    adapterma.add(MessageItem(
                        document.toObject(TextMessage::class.java)!!,
                        document.id, this
                    ))

                }else{
                        val m= document.toObject(ImageMessage::class.java)

                    adapterma.add(
                        Item_message_reciver(
                            document.toObject(TextMessage::class.java)!!,
                            document.id, this
                        ))
                }

            }else{
                adapterma.add(
                    Image_item(document.toObject(ImageMessage::class.java)!!
                        ,document.id,
                        this)

                )
            }
            }

        }
    }

    private fun SendMessage( channel_id:String, textMessage: TextMessage) {
        chatChannelcollectin.document(channel_id).collection("Message").add(textMessage)


            }

    private fun ChatChannel(oncomplete:(channel:String)->Unit) {
        //val user_id=FirebaseAuth.getInstance().uid!!
        firebaseFirestore.collection("Users")
            .document(user_id)
            .collection("chatchannel")
            .document(athor_id).get()
            .addOnSuccessListener {
                if (it.exists()){
                    oncomplete(it["channel_id"]as String)

                    return@addOnSuccessListener
                }else{

                    val chatchannel_id=firebaseFirestore.collection("Users").document()
                    firebaseFirestore.collection("Users")
                        .document(user_id)
                        .collection("chatchannel")
                        .document(athor_id).set(mapOf("channel_id" to chatchannel_id.id))

                    firebaseFirestore.collection("Users")
                        .document(athor_id)
                        .collection("chatchannel")
                        .document(user_id).set(mapOf("channel_id" to chatchannel_id.id))

                    oncomplete(chatchannel_id.id)

                }

            }


    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_chatActivity_back->{
                finish()
            }
            R.id.bt_chat_Activity_send->{
                ed_chat_Activity_message.setText("")
                Toast.makeText(this,"Send message to"+athor_id,Toast.LENGTH_SHORT).show()


            }
            R.id.fb_chat_Activity_photo->{

                    val intent: Intent = Intent().apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                    }
                    startActivityForResult(Intent.createChooser(intent,"Select Image"),PH_REC_COD)


                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PH_REC_COD && resultCode== Activity.RESULT_OK&&data!=null){
            //convert image to bit

            val selectimage=data.data
            val SelectBitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,selectimage)
            val  outputstream= ByteArrayOutputStream()
            SelectBitmap.compress(Bitmap.CompressFormat.JPEG,20,outputstream)
            val selectImageToBitye=outputstream.toByteArray()
            uploadprofileimage(selectImageToBitye){path ->
                userInf {
                    val message=ImageMessage(path,user_id,it.name,it.name,Calendar.getInstance().time,athor_id,MessageType.Image)


                ChatChannel {
                    chatChannelcollectin.document(it)
                        .collection("Message").add(message)
                }

}
            }


        }
    }
    private fun uploadprofileimage(selectImageToBitye: ByteArray,onsuccess:(imagePath:String)->Unit) {
        val progress= ProgressDialog(this)
        progress.show()
        val ref=storage.child("images/${UUID.nameUUIDFromBytes(selectImageToBitye)}")
        ref.putBytes(selectImageToBitye).addOnCompleteListener(){
            if(it.isSuccessful){
                progress.dismiss()

                onsuccess(ref.path) }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
       if (ed_chat_Activity_message.text.isNotEmpty()){
            bt_chat_Activity_send.visibility=View.VISIBLE
        }else{
           bt_chat_Activity_send.visibility=View.INVISIBLE

       }
    }
    private fun userInf(oncomplete:(User)->Unit){
        reference.get().addOnSuccessListener {
            if(it.data!=null){
                oncomplete(it.toObject(User::class.java)!!)
            }}

    }
    private fun sendNotifaction(token:String,username:String,message:String){
      val  data=DataModel(username,message)
        val notif=NotificationModel(username,message)
        val rootmodel=RootModel(token,notif,data)
        val responsbady = apiserver.sendNotification(rootmodel)
        responsbady.enqueue(object: Callback<com.mostafabor3e.messenger.Model.Response> {
            override fun onResponse(
                call: Call<com.mostafabor3e.messenger.Model.Response>?,
                response: Response<com.mostafabor3e.messenger.Model.Response>?
            ) {
                Toast.makeText(this@Chat_Activity,"sueccess"+response?.message(),Toast.LENGTH_LONG).show()
            }

            override fun onFailure(
                call: Call<com.mostafabor3e.messenger.Model.Response>?,
                t: Throwable?
            ) {
                Log.d("falu",t?.message.toString())

            }
        })}


}

