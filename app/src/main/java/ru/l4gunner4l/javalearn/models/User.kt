package ru.l4gunner4l.javalearn.models

data class User(
        val id: Long,
        var name: String,
        var email: String,
        var password: String,
        var currentLvl: Int = 1
) {
}