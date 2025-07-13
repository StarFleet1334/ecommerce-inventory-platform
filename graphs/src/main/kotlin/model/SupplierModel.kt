package org.example.model

import java.math.BigDecimal

data class SupplierModel(
    var first_name: String = "",
    var last_name: String = "",
    var email: String = "",
    var phone_number: String = "",
    var latitude: BigDecimal = BigDecimal.ZERO,
    var longitude: BigDecimal = BigDecimal.ZERO
)
