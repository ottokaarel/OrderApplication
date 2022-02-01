package app

import model.Order

import model.OrderDao
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class OrderController(var dao: OrderDao) {

    @GetMapping("orders")
    fun getAllOrders(): List<Order> {
        return dao.findAllOrders()
    }

    @GetMapping("orders/{id}")
    fun getOrder(@PathVariable id: Long?): Order {
        return dao.findOrderById(id)
    }

    @PostMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    fun saveOrder(@RequestBody order: @Valid Order?): Order {
        return dao.saveOrder(order!!)
    }

    @DeleteMapping("orders/{id}")
    fun deleteOrder(@PathVariable id: Long?) {
        dao.deleteOrder(id)
    }
}