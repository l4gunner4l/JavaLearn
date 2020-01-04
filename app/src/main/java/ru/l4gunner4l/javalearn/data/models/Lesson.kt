package ru.l4gunner4l.javalearn.data.models

data class Lesson(
        val number: Int,
        val name: String,
        val text: String,
        val tests: MutableList<Int>
)