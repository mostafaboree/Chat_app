package com.mostafabor3e.messenger

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIDService: FirebaseInstanceIdService() {
    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val  token=FirebaseInstanceId.getInstance().token
        Log.d("token",token.toString())
    }
}