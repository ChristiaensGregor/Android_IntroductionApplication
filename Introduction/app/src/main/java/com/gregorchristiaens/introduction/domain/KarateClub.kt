package com.gregorchristiaens.introduction.domain

class KarateClub() {
    lateinit var id: String
    lateinit var admin: String

    constructor(_id: String, _admin: String) : this() {
        id = _id
        admin = _admin
    }
}