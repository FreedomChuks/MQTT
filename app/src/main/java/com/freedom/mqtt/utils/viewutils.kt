package com.freedom.mqtt.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.toast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

fun logs(message: String){
    Log.d("logs",message)
}