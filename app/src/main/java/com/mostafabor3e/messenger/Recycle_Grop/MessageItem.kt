package com.mostafabor3e.messenger.Recycle_Grop

import android.content.Context
import android.os.Build
import com.mostafabor3e.messenger.Model.TextMessage
import com.mostafabor3e.messenger.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_reciver_mass.*
import kotlinx.android.synthetic.main.item_sender_message.*
import java.text.DateFormat
import java.text.Format
import java.time.format.DateTimeFormatter

class MessageItem(val message:TextMessage,val message_id:String,val contex:Context):Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv_message_sender.text=message.text

            viewHolder.tv_date_sender.text=android.text.format.DateFormat.format("hh:mm a",message.date)


    }

    override fun getLayout(): Int {
        return R.layout.item_sender_message
    }
}