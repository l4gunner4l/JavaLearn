package ru.l4gunner4l.javalearn.models

data class Lesson
(
    val number:Int,
    val name:String,
    var isAvailable: Boolean = false,
    private var fails: Int?
) {
    val starsCount = when (fails) {
        null -> 0
        0 -> 3
        1 -> 2
        2 -> 1
        else -> 0
    }

}