package model

import org.springframework.jdbc.core.JdbcTemplate
import app.OrderRowHandler
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import kotlin.Throws
import com.fasterxml.jackson.core.JsonProcessingException
import org.springframework.stereotype.Repository
import java.io.Serializable
import javax.validation.Validation

@Repository
class OrderDao(var template: JdbcTemplate) {
    fun findOrderById(idToFind: Long?): Order {
        val sql = "SELECT * FROM orders LEFT JOIN orderlines ON orders.id = orderlines.orderid WHERE orders.id = ?"
        val handler = OrderRowHandler()
        template.query(sql, handler, idToFind)
        return handler.getResult()[0]
    }

    fun findAllOrders(): List<Order> {
        val sql = "SELECT * FROM orders LEFT JOIN orderlines ON orders.id = orderlines.orderid"
        val handler = OrderRowHandler()
        template.query(sql, handler)
        return handler.getResult()
    }

    fun saveOrder(order: Order): Order {
        val newOrder = addNewOrder(order)
        if (newOrder.orderRows != null) {
            addNewOrderRows(newOrder)
        }
        return newOrder
    }

    private fun addNewOrder(order: Order): Order {
        val data = java.util.Map.of("ordernumber", order.orderNumber)
        val number = SimpleJdbcInsert(template)
            .withTableName("orders")
            .usingGeneratedKeyColumns("id")
            .executeAndReturnKey(data)
        order.id = number.toLong()
        return order
    }

    private fun addNewOrderRows(order: Order) {
        for (row in order.orderRows!!) {
            val data2: Map<String, Serializable?> = java.util.Map.of(
                "itemName", row.itemName,
                "quantity", row.quantity,
                "price", row.price,
                "orderId", order.id
            )
            val rowNumber = SimpleJdbcInsert(template)
                .withTableName("orderlines")
                .usingGeneratedKeyColumns("orderLineId")
                .executeAndReturnKey(data2)
            row.id = rowNumber.toLong()
        }
    }

    fun deleteOrder(id: Long?) {
        val sql = "DELETE FROM orders WHERE id = (?)"
        template.update(sql, id)
    }

    @Throws(JsonProcessingException::class)
    fun validate(order: Order): ValidationErrors {
        val validator = Validation.buildDefaultValidatorFactory().validator
        val violations = validator.validate(order)
        val errors = ValidationErrors()
        for (violation in violations) {
            errors.addErrorMessage(violation.message)
        }
        return errors
    }


}