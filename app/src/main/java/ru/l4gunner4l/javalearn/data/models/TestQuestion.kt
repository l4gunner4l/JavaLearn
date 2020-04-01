package ru.l4gunner4l.javalearn.data.models

import java.io.Serializable

class TestQuestion(
        var text: String,
        var code: String?,
        var answers: MutableList<String>,
        var rightAnswer: Int
) : Serializable