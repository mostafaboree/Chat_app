package com.mostafabor3e.messenger.Model

import java.util.*

data class TextMessage(val text:String,
                       override val sender:String,
                       override val senderName:String,
                       override val recipientId:String,
                       override val date:Date,
                       override val rec: String,
                       override val type:String=MessageType.Text
):Message {
    constructor(): this("","","","",Date(),"","")

}
