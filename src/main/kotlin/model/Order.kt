package model

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class Order (
    var id: Long? = null,
    @get:NotNull @get:Size(min = 2) val orderNumber:  String? = null,
    @get:Valid var orderRows: MutableList<OrderRow>? = null

)