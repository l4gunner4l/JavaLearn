package ru.l4gunner4l.javalearn.utils.extensions

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View

fun View.hide(){
    val animator = ObjectAnimator.ofFloat(
            this,
            "y",
            this.height.toFloat()
    )
    animator.duration = 1000
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(animator)
    animatorSet.start()
}
fun View.show(){
    val animator = ObjectAnimator.ofFloat(
            this,
            "y",
            (-1)*this.height.toFloat()
    )
    animator.duration = 1000
    val animatorSet = AnimatorSet()
    animatorSet.playTogether(animator)
    animatorSet.start()
}