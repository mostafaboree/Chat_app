package com.mostafabor3e.messenger.Model

import java.util.*

interface Message{
    val sender:String
    val recipientId:String
    val senderName:String
    val rec:String
    val type:String
    val date:Date
}