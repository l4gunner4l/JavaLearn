package ru.l4gunner4l.javalearn.data.models

class User {
    lateinit var id: String
        private set
    lateinit var name: String
    lateinit var email: String
    var imageUrl: String? = null
    var level = 1
        private set
    var starsList = mutableListOf(0)
        private set
    // TODO avatar

    constructor() {}
    constructor(id: String, name: String, email: String) {
        this.id = id
        this.name = name
        this.email = email
        starsList = mutableListOf(0)
    }
    constructor(id: String, name: String, email: String, imageUrl: String?) {
        this.id = id
        this.name = name
        this.email = email
        this.imageUrl = imageUrl
        starsList = mutableListOf(0)
    }
    constructor(id: String, name: String, email: String, imageUrl: String?, starsList: MutableList<Int>) {
        this.id = id
        this.name = name
        this.email = email
        this.imageUrl = imageUrl
        this.level = starsList.size
        this.starsList = starsList
    }

    fun lvlUp(){
        this.level++
        starsList.add(0)
    }

    override fun toString() = "User{" +
            "id=" + this.id +
            ", name=" + this.name +
            ", email=" + this.email +
            ", lvl=" + this.level + "}"

}