package com.example.emptyviewsactivity.DB

data class User(
    var role: String = "",
    var userId: String? = "",
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var isItActive: Boolean = false,
)
