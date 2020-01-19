package ru.l4gunner4l.javalearn.utils

import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import ru.l4gunner4l.javalearn.R

/**
 * Object for Constants and util functions
*/

object Utils {

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

}