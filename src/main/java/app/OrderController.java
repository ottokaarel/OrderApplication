package app;

import model.Order;
import dao.OrderDao;
import model.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {

    public OrderDao dao;

    public OrderController(OrderDao dao) {
        this.dao = dao;
    }

    @GetMapping( "/api/orders")
    public List<Order> getOrders() {
        return dao.findAll();
    }

    @GetMapping("/api/orders/{id}")
    public Order getOrder(@PathVariable Long id) {
        return dao.findById(id);
    }

    @PostMapping("/api/orders")
    @ResponseStatus(HttpStatus.OK)
    public Order saveOrder(@RequestBody @Valid Order order) {
        return dao.save(order);
    }

    @DeleteMapping("/api/orders/{id}")
    public void deleteOrder(@PathVariable Long id) {
        dao.delete(id);
    }

    @GetMapping("/api/version")
    public String getVersion() {
        return "Version";
    }

    @GetMapping("/api/users")
    public String getAllUsers() {
        return "All users";
    }

    @GetMapping("/api/users/{userName}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.name")
    public String getUserByName(@PathVariable String userName) {
        return userName;
    }

    @ExceptionHandler
    public String handleErrors(Exception e) {
        return "error";
    }
}
