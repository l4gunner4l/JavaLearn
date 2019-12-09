package ru.l4gunner4l.javalearn.models

data class User(
        var name: String,
        var surname: String,
        var email: String,
        var password: String
) {
    val id: Long = 0
}