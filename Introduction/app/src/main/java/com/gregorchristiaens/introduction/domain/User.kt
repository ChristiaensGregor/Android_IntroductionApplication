package com.gregorchristiaens.introduction.domain

class User() {

    var id: String = ""
    var email: String = ""
    var displayName: String = ""
    var joinDate: String = ""
    var karateClubId: String? = null

    constructor(_id: String, _email: String?, _displayName: String?) : this() {
        id = _id
        if (_email != null) {
            email = _email
        }
        if (_displayName != null) {
            displayName = _displayName
        }
    }
}