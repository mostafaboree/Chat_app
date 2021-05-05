package com.mostafabor3e.messenger.Model

import java.util.*

class ImageMessage(
    val image:String,
    override val sender:String,
    override val senderName:String,
    override val recipientId:String,
override val date: Date,
    override val rec: String,
    override val type:String=MessageType.Image
):Message {
    constructor(): this("","","","",Date(),"","")


}