package com.example.emptyviewsactivity.DB

data class Order(
    var orderId: String = "",
    val userId: String? = "",
    val userEmail: String? = "",
    val totalPrice: Double? = 0.0,
    val status: String? = "bekliyor"
)