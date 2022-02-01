package dao;

import com.fasterxml.jackson.core.JsonProcessingException;

import model.Order;
import model.ValidationErrors;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;


@Repository
public class OrderDao {

    @PersistenceContext
    private EntityManager em;


    public Order findById(Long idToFind) {
        return em.find(Order.class, idToFind);
    }

    public List<Order> findAll() {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    @Transactional
    public Order save(Order order) {
        if (order.getId() == null) {
            em.persist(order);
        } else {
            em.merge(order);
        }
        return order;
    }

    @Transactional
    public void delete(Long id) {
        Order order = findById(id);
        if (order != null) {
            em.remove(order);
        }
    }

    public ValidationErrors validate(Order order) throws JsonProcessingException {
        var validator = Validation.buildDefaultValidatorFactory().getValidator();

        var violations = validator.validate(order);

        ValidationErrors errors = new ValidationErrors();

        for (ConstraintViolation<Order> violation : violations) {
            errors.addErrorMessage(violation.getMessage());
        }

        return errors;
    }


}
