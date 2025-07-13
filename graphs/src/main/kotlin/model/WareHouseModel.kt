package org.example.model

import java.math.BigDecimal

data class WareHouseModel(
    var ware_house_name: String = "",
    var refrigerated: Boolean = false,
    var min_stock_level: Int = 0,
    var max_stock_level: Int = 0,
    var latitude: BigDecimal = BigDecimal.ZERO,
    var longitude: BigDecimal = BigDecimal.ZERO
)
