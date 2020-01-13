package ru.l4gunner4l.javalearn.utils.extensions

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.widget.AppCompatRadioButton

fun AppCompatRadioButton.setMyStyle(colorChecked: Int, colorUnchecked: Int) {
    val colorStateList = ColorStateList(
            arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
            ),
            intArrayOf(
                Color.rgb(155, 168, 185),
                Color.rgb(255, 115, 21)
            )
    )

    this.supportButtonTintList = colorStateList
    this.textSize = 15f
    this.setTextColor(resources.getColor(ru.l4gunner4l.javalearn.R.color.colorTextDark))
}