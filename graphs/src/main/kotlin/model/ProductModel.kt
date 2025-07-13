package org.example.model

import java.math.BigDecimal

data class ProductModel(
    var product_name: String = "",
    var sku: String = "",
    var product_id: String = "",
    var product_price: BigDecimal = BigDecimal.ZERO,
    var product_description: String = ""
)
