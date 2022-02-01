package model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotNull


@JsonIgnoreProperties("id", "orderId")
data class OrderRow (
    var id: Long? = null,
    @get:NotNull var itemName:  String? = null,
    @get:NotNull @get:DecimalMin("1") var quantity:  Int? = null,
    @get:NotNull @get:DecimalMin("1") var price:  Int? = null,
    var orderId: Long? = null
)