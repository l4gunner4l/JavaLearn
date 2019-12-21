package ru.l4gunner4l.javalearn.models

import com.google.firebase.auth.FirebaseUser

class User {
    lateinit var id: String
        private set
    lateinit var name: String
    lateinit var email: String
    var level = 1
        private set

    constructor() {}
    constructor(id: String, name: String, email: String) {
        this.id = id
        this.name = name
        this.email = email
    }
    constructor(id: String, name: String, email: String, level: Int) {
        this.id = id
        this.name = name
        this.email = email
        this.level = level
    }

    fun lvlUp(){
        level++
    }

    override fun toString() = "User{" +
            //"id=" + this.id +
            ", name=" + this.name +
            ", email=" + this.email +
            ", lvl=" + this.level + "}"

    companion object {
        fun create(firebaseUser: FirebaseUser, name: String) : User{
            return User(firebaseUser.uid, name, firebaseUser.email!!)
        }
    }



}