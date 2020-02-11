package ru.l4gunner4l.javalearn.data.models

class User {
    val id: String
    var name: String
    var email: String
    var avatarName: String? = null
    var level: Int = 1
        private set
    var starsList = mutableListOf(0)
        private set

    constructor(id: String, name: String, email: String) {
        this.id = id
        this.name = name
        this.email = email
        starsList = mutableListOf(0)
    }
    constructor(id: String, name: String, email: String, avatarName: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.avatarName = avatarName
        starsList = mutableListOf(0)
    }
    constructor(id: String, name: String, email: String, avatarName: String?, starsList: MutableList<Int>) {
        this.id = id
        this.name = name
        this.email = email
        this.avatarName = avatarName
        this.level = starsList.size
        this.starsList = starsList
    }

    override fun toString() = "User{" +
            "id=" + this.id +
            ", name=" + this.name +
            ", email=" + this.email +
            ", lvl=" + this.level + "}"

}