package ru.l4gunner4l.javalearn.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.l4gunner4l.javalearn.R

/**
 * Object for Constants and util functions
*/

object Utils {

    const val ADMIN_EMAIL = "155nimix@mail.ru"

    const val PASSWORD_PATTERN = "^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$"

    fun showToast(context: Context, text:String, duration:Int){
        val toast = Toast.makeText(context, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view.background.setColorFilter(context.getColor(R.color.colorBackgroundDark), PorterDuff.Mode.SRC_IN)
        toast.view.findViewById<TextView>(android.R.id.message)
                .setTextColor(context.getColor(R.color.colorAccent))
        toast.show()
    }

    fun showToast(context: Context, text:Int, duration:Int){
        val toast = Toast.makeText(context, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.view.background.setColorFilter(context.getColor(R.color.colorBackgroundDark), PorterDuff.Mode.SRC_IN)
        toast.view.findViewById<TextView>(android.R.id.message)
                .setTextColor(context.getColor(R.color.colorAccent))
        toast.show()
    }

    fun isInternetConnection(context: Context): Boolean {
        var isWifi = false
        var isMobData = false
        val connectManager = (context as AppCompatActivity).getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectManager.allNetworkInfo
        for (info in networkInfos){
            if (info.typeName == "WIFI")
                if (info.isConnected)
                    isWifi = true
            if (info.typeName == "MOBILE")
                if (info.isConnected)
                    isMobData = true
        }
        return isWifi || isMobData

    }

}