package ru.l4gunner4l.javalearn.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.view.setPadding

fun AppCompatRadioButton.setMyStyle() {
    val colorStateList = ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(Color.rgb(155, 168, 185), Color.rgb(255, 115, 21))
    )

    this.supportButtonTintList = colorStateList
    this.textSize = 15f
    this.setTextColor(resources.getColor(ru.l4gunner4l.javalearn.R.color.colorTextDark))
    this.setPadding(resources.getDimension(ru.l4gunner4l.javalearn.R.dimen.margin_8dp).toInt())
}