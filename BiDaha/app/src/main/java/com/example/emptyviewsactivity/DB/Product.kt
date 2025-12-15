package com.example.emptyviewsactivity.DB

import java.io.Serializable

data class Product(
    var productId: String? = null,
    val sellerId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val price: Double? = null,
    val imageUrl: String? = null,
): Serializable
