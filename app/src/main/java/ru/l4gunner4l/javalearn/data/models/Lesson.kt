package ru.l4gunner4l.javalearn.data.models

data class Lesson(
        var number: Int,
        var name: String,
        var text: String,
        var tests: MutableList<Int>
)