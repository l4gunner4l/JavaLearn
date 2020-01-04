package ru.l4gunner4l.javalearn.data.models

data class Question(
        val questionText: String,
        val answers: List<String>,
        val correctAnswerIndex: Int
)