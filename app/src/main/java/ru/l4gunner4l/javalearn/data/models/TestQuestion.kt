package ru.l4gunner4l.javalearn.data.models

data class TestQuestion(
        val question: String,
        val answers: MutableList<String>,
        val rightAnswer: Int
)