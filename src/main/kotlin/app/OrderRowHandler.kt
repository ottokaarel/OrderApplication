package app


import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor
import model.Order
import org.springframework.jdbc.core.RowCallbackHandler
import kotlin.Throws
import java.sql.SQLException
import java.sql.ResultSet
import model.OrderRow
import java.util.ArrayList

@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderRowHandler : RowCallbackHandler {
    var result = ArrayList<Order>()
    @Throws(SQLException::class)
    override fun processRow(rs: ResultSet) {
        val orderId = rs.getLong("id")
        val orderLineId = rs.getLong("orderlineid")
        val orderNumber = rs.getString("ordernumber")
        val itemName = rs.getString("itemname")
        val quantity = rs.getInt("quantity")
        val price = rs.getInt("price")
        var orderExists = false
        val orderRow = OrderRow(orderLineId, itemName, quantity, price, orderId)
        for (order in result) {
            if (order.id == orderId) {
                val orderRows: List<OrderRow>? = order.orderRows
                val newList: MutableList<OrderRow> = ArrayList(orderRows)
                newList.add(orderRow)
                order.orderRows = newList
                orderExists = true
                break
            }
        }
        if (!orderExists) {
            val order = Order(orderId, orderNumber, java.util.List.of(orderRow))
            result.add(order)
        }
    }

    fun getResult(): List<Order> {
        return result
    }
}