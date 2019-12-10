package ru.l4gunner4l.javalearn.utils

import android.util.Log
import java.util.regex.Pattern

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


}