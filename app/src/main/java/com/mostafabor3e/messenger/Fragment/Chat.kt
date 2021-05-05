package com.mostafabor3e.messenger.Fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.mostafabor3e.messenger.Chat_Activity
import com.mostafabor3e.messenger.Model.User
import com.mostafabor3e.messenger.R
import com.mostafabor3e.messenger.Recycle_Grop.ChatItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_chat.view.*


@Suppress("UNREACHABLE_CODE")
class Chat : Fragment() {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
 val  firebaseFirestore:FirebaseFirestore by lazy {
    FirebaseFirestore.getInstance()
}
    private lateinit var sectin:Section
    lateinit var id:String
    val search =ed_search
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_chat, container, false)

        val texttitle=activity?.findViewById<TextView>(R.id.title_activ)
        texttitle?.text="Chat"

        chatlisener{
            initalRecycel(it)

        }
       view.ed_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
if (p0!!.isNotEmpty()) {
    search(p0.toString())
}

               }


            override fun afterTextChanged(p0: Editable?) {
                //Toast.makeText(activity!!,"after",Toast.LENGTH_LONG).show()
            }
        })

        return view
    }

    private fun chatlisener(onLisener:(List<Item>)->Unit) {

        firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().currentUser!!.uid).
        collection("chatchannel")

            .addSnapshotListener{value: QuerySnapshot?,
                                 error: FirebaseFirestoreException? ->

                    val items= mutableListOf<Item>()
                value!!.documents.forEach {
                   //val user= it.toObject(User::class.java)
                     val id=it.id
                    items.add(ChatItem(it.toObject(User::class.java)!!,id,activity!!))
                }
                onLisener(items)



        }
            
        }
    fun initalRecycel(item:List<Item>) {

        rec_chat.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = GroupAdapter<ViewHolder>().apply {
                sectin = Section(item)
                add(sectin)
                setOnItemClickListener(onclick)


            }
        }
    }
        val onclick=OnItemClickListener{item,view ->
            if (item is ChatItem){
               val name= item.user.name
                val  profileimage=item.user.profile_image
                val token=item.user.token
                val intent=Intent(activity,Chat_Activity::class.java)
                intent.putExtra("username",name)
                intent.putExtra("profileimage",profileimage)
                intent.putExtra("id",item.uid)
                intent.putExtra("token",token)

                activity!!.startActivity(intent)
            }



        }

    fun search(s:String){
        if (s.isNotEmpty()){

    val query= firebaseFirestore.collection("Users")
        .orderBy("name").
        startAt(s)
        .endAt(s+"\uf8ff")

    query .addSnapshotListener { value: QuerySnapshot?,
                                 error: FirebaseFirestoreException? ->
        if (value!!.isEmpty){
            Toast.makeText(activity!!,"value empty",Toast.LENGTH_LONG).show()

        }
        val items = mutableListOf<Item>()
        value!!.documents.forEach {
            //val user= it.toObject(User::class.java)
            val id = it.id
          ///  Toast.makeText(activity!!,"user_id"+id,Toast.LENGTH_LONG).show()


            items.add(ChatItem(it.toObject(User::class.java)!!, id, activity!!))
        }
        initalRecycel(items)


    }}

    }
}







