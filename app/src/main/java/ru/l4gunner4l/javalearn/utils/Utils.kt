package ru.l4gunner4l.javalearn.utils

import android.util.Log
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Object for Constants and util functions
*/

object Utils {

    private const val PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"


    fun isValidInfo(email: String?, password: String?): Boolean {
        Log.i("M_MAIN", "isValidEmail($email)=${isValidEmail(email)}; isValidPassword($password)=${isValidPassword(password)}")
        return isValidEmail(email) && isValidPassword(password)
    }

    private fun isValidEmail(email: String?): Boolean{
        return when (email) {
            null -> false
            else -> android.util.Patterns.EMAIL_ADDRESS
                    .matcher(email).matches()
        }
    }
    private fun isValidPassword(password: String?): Boolean {
        return when {
            password == null -> false
            password.length < 8 -> false
            else -> Pattern.compile(PASSWORD_PATTERN)
                    .matcher(password).matches()
        }
    }

}