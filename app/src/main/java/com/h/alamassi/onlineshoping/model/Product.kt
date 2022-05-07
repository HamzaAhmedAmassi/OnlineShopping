package com.h.alamassi.onlineshoping.model

data class Product(
    var id: String = "",
    var catId: String = "",
    var name: String = "",
    var description: String = "",
    var Image: String = "",
    var price: Double = 0.0,
    var rate: Double = 0.0,
    var quantity: Int = 0,
    var location: String = "",
    var taxPercent: Double = 0.0
) {

}
