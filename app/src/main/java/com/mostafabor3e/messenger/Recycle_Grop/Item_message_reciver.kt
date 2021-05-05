package com.mostafabor3e.messenger.Recycle_Grop

import android.content.Context
import com.mostafabor3e.messenger.Model.TextMessage
import com.mostafabor3e.messenger.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_reciver_mass.*

class Item_message_reciver(val message: TextMessage,val message_id:String, val contex: Context):Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.tv_message_reciver.text=message.text
        viewHolder.tv_date_reciver.text= android.text.format.DateFormat.format("hh:mm a",message.date)
    }

    override fun getLayout(): Int {
        return R.layout.item_reciver_mass
    }
}